package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayDecisionMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import cn.springcloud.gray.server.dao.repository.GrayDecisionRepository;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable) {
        Page<GrayDecisionDO> entities = repository.findAllByPolicyId(policyId, pageable);
        return PaginationUtils.convert(pageable, entities, grayDecisionMapper);
    }
}
