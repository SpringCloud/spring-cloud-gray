package cn.springcloud.gray.server.service;

import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.server.dao.mapper.GrayInstanceMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import cn.springcloud.gray.server.dao.repository.GrayInstanceRepository;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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


    public void deleteReactById(String id) {
        delete(id);
        grayPolicyService.findByInstanceId(id).forEach(entity -> {
            grayPolicyService.deleteReactById(entity.getId());
        });
    }

    public List<GrayInstance> findAllByGrayStatus(GrayStatus grayStatus) {
        return repository.findAllByGrayStatus(grayStatus.name());
    }
}
