package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayDecisionMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import cn.springcloud.gray.server.dao.repository.GrayDecisionRepository;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrayDecisionService extends AbstraceCRUDService<GrayDecision, GrayDecisionRepository, GrayDecisionDO, Long> {

    @Autowired
    private GrayDecisionRepository repository;
    @Autowired
    private GrayDecisionMapper grayDecisionMapper;

    @Override
    protected GrayDecisionRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayDecision, GrayDecisionDO> getModelMapper() {
        return grayDecisionMapper;
    }

    public List<GrayDecision> findByPolicyId(Long policyId) {
        return grayDecisionMapper.dos2models(repository.findByPolicyId(policyId));

    }

    public void deleteAllByPolicyId(Long policyId) {
        repository.deleteAllByPolicyId(policyId);
    }
}
