package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayDecisionMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import cn.springcloud.gray.server.dao.repository.GrayDecisionRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.query.GrayDecisionQuery;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<GrayDecision> findAllEnabledByPolicyId(Long policyId) {
        return grayDecisionMapper.dos2models(repository.findByPolicyIdAndDelFlag(policyId, false));

    }

    public void deleteAllByPolicyId(Long policyId) {
        repository.deleteAllByPolicyId(policyId);
    }

    public Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable) {
        Page<GrayDecisionDO> entities = repository.findAllByPolicyId(policyId, pageable);
        return PaginationUtils.convert(pageable, entities, grayDecisionMapper);
    }

    public Page<GrayDecision> queryGrayDecisions(GrayDecisionQuery query, Pageable pageable) {
        Page<GrayDecisionDO> page = repository.findAll(createSpecification(query), pageable);
        return PaginationUtils.convert(pageable, page, grayDecisionMapper);
    }


    private Specification<GrayDecisionDO> createSpecification(GrayDecisionQuery grayDecisionQuery) {
        return new Specification<GrayDecisionDO>() {

            @Override
            public Predicate toPredicate(Root<GrayDecisionDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (Objects.nonNull(grayDecisionQuery.getPolicyId())) {
                    predicates.add(cb.equal(root.get("policyId").as(Long.class), grayDecisionQuery.getPolicyId()));
                }
                if (Objects.nonNull(grayDecisionQuery.getDelFlag()) && !Objects.equals(grayDecisionQuery.getDelFlag(), DelFlag.ALL)) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), grayDecisionQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }


}
