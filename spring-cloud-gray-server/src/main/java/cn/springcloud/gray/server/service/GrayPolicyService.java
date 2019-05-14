package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayPolicyMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import cn.springcloud.gray.server.dao.repository.GrayPolicyRepository;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GrayPolicyService extends AbstraceCRUDService<GrayPolicy, GrayPolicyRepository, GrayPolicyDO, Long> {
    @Autowired
    private GrayPolicyRepository repository;
    @Autowired
    private GrayDecisionService grayDecisionService;
    @Autowired
    private GrayPolicyMapper grayPolicyMapper;

    @Override
    protected GrayPolicyRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayPolicy, GrayPolicyDO> getModelMapper() {
        return grayPolicyMapper;
    }

    public List<GrayPolicy> findByInstanceId(String instanceId) {
        return grayPolicyMapper.dos2models(repository.findByInstanceId(instanceId));
    }

    public void deleteByInstanceId(String instanceId) {
        findByInstanceId(instanceId).forEach(entity -> {
            delete(entity.getId());
            grayDecisionService.deleteAllByPolicyId(entity.getId());
        });
    }

    @Transactional
    public void deleteReactById(Long id) {
        delete(id);
        grayDecisionService.deleteAllByPolicyId(id);
    }

    public Page<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId, Pageable pageable) {
        Page<GrayPolicyDO> entities = repository.findAllByInstanceId(instanceId, pageable);
        return PaginationUtils.convert(pageable, entities, grayPolicyMapper);
    }
}
