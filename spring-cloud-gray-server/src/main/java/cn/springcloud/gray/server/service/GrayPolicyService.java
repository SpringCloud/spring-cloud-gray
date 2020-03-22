package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayPolicyMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import cn.springcloud.gray.server.dao.repository.GrayPolicyRepository;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
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


    @Transactional
    public void deleteReactById(Long id) {
        delete(id);
        grayDecisionService.deleteAllByPolicyId(id);
    }


    public Page<GrayPolicy> listGrayPoliciesByNamespace(String namespace, Pageable pageable) {
        Page<GrayPolicyDO> entities = repository.findAllByNamespace(namespace, pageable);
        return PaginationUtils.convert(pageable, entities, grayPolicyMapper);
    }

    public List<GrayPolicy> listGrayPoliciesByNamespace(String namespace) {
        List<GrayPolicyDO> entities = repository.findAllByNamespace(namespace);
        return grayPolicyMapper.dos2models(entities);
    }

    public List<GrayPolicy> findAllModel(Iterable<Long> policyIds, Boolean delFlag) {
        return grayPolicyMapper.dos2models(repository.findAllByIdInAndDelFlag(policyIds, delFlag));
    }
}
