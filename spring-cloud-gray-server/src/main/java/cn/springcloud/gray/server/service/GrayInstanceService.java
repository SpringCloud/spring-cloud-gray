package cn.springcloud.gray.server.service;

import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.dao.mapper.GrayInstanceMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import cn.springcloud.gray.server.dao.repository.GrayInstanceRepository;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrayInstanceService extends AbstraceCRUDService<GrayInstance, GrayInstanceRepository, GrayInstanceDO, String> {

    @Autowired
    private GrayInstanceRepository repository;
    @Autowired
    private GrayPolicyService grayPolicyService;
    @Autowired
    private GrayInstanceMapper grayInstanceMapper;


    @Override
    protected GrayInstanceRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayInstance, GrayInstanceDO> getModelMapper() {
        return grayInstanceMapper;
    }

    public List<GrayInstance> findByServiceId(String serviceId) {
        return grayInstanceMapper.dos2models(repository.findByServiceId(serviceId));
    }

    @Transactional
    public void deleteByServiceId(String serviceId) {
        findByServiceId(serviceId).forEach(entity -> {
            delete(entity.getInstanceId());
            grayPolicyService.deleteByInstanceId(entity.getInstanceId());
        });
    }


    @Transactional
    public void deleteReactById(String id) {
        delete(id);
        grayPolicyService.findByInstanceId(id).forEach(entity -> {
            grayPolicyService.deleteReactById(entity.getId());
        });
    }

    @Override
    public void saveModel(GrayInstance grayInstance) {
        grayInstance.setLastUpdateDate(new Date());
        super.saveModel(grayInstance);
    }

    public List<GrayInstance> findAllByStatus(GrayStatus grayStatus, Collection<InstanceStatus> instanceStatusList) {
        String[] instanceStatusAry = toArray(instanceStatusList);
        return grayInstanceMapper.dos2models(
                repository.findAllByGrayStatusAndInstanceStatusIn(grayStatus.name(), instanceStatusAry));
    }

    public Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable) {
        Page<GrayInstanceDO> entities = repository.findAllByServiceId(serviceId, pageable);
        return PaginationUtils.convert(pageable, entities, grayInstanceMapper);
    }

    public List<GrayInstance> findByServiceId(String serviceId, Collection<InstanceStatus> instanceStatusList) {
        String[] instanceStatusAry = toArray(instanceStatusList);

        return dos2models(repository.findAllByServiceIdAndInstanceStatusIn(serviceId, instanceStatusAry));

    }

    public List<GrayInstance> listGrayInstancesByNormalInstanceStatus(Collection<InstanceStatus> instanceStatusList) {
        String[] instanceStatusAry = toArray(instanceStatusList);
        return dos2models(repository.findAllByGrayStatusAndInstanceStatusIn(GrayStatus.OPEN.name(), instanceStatusAry));
    }

    private String[] toArray(Collection<InstanceStatus> instanceStatusList) {
        return instanceStatusList
                .stream()
                .map(InstanceStatus::name)
                .collect(Collectors.toList())
                .toArray(new String[instanceStatusList.size()]);
    }

    public List<GrayInstance> findAllByEvictableRecords(
            int lastUpdateDateExpireDays, Collection<InstanceStatus> evictionInstanceStatus) {
        Date lastUpdateDate = Date.from(
                LocalDateTime
                        .now()
                        .minusDays(lastUpdateDateExpireDays)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
        String[] instanceStatusAry = toArray(evictionInstanceStatus);
        return dos2models(repository.findAllByLastUpdateDateBeforeAndInstanceStatusIn(lastUpdateDate, instanceStatusAry));
    }
}
