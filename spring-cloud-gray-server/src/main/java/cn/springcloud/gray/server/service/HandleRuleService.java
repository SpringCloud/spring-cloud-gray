package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.HandleRuleMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.HandleRuleDO;
import cn.springcloud.gray.server.dao.repository.HandleRuleRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.domain.query.HandleRuleQuery;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author saleson
 * @date 2020-05-31 10:03
 */
@Service
public class HandleRuleService extends AbstraceCRUDService<HandleRule, HandleRuleRepository, HandleRuleDO, Long> {

    @Autowired
    private HandleRuleRepository handleRuleRepository;
    @Autowired
    private HandleRuleMapper handleRuleMapper;


    @Override
    protected HandleRuleRepository getRepository() {
        return handleRuleRepository;
    }

    @Override
    protected ModelMapper<HandleRule, HandleRuleDO> getModelMapper() {
        return handleRuleMapper;
    }

    public Page<HandleRule> findAllModels(HandleRuleQuery handleRuleQuery, Pageable pageable) {
        Page<HandleRuleDO> page = handleRuleRepository.findAll(createSpecification(handleRuleQuery), pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());

    }

    public List<HandleRule> findAllModels(HandleRuleQuery handleRuleQuery) {
        return getModelMapper().dos2models(handleRuleRepository.findAll(createSpecification(handleRuleQuery)));
    }


    private Specification<HandleRuleDO> createSpecification(HandleRuleQuery handleRuleQuery) {
        return new Specification<HandleRuleDO>() {

            @Override
            public Predicate toPredicate(Root<HandleRuleDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(handleRuleQuery.getType())) {
                    predicates.add(cb.equal(root.get("type").as(String.class), handleRuleQuery.getType()));
                }
                if (StringUtils.isNotEmpty(handleRuleQuery.getNamespace())) {
                    predicates.add(cb.equal(root.get("namespace").as(String.class), handleRuleQuery.getNamespace()));
                }
                if (StringUtils.isNotEmpty(handleRuleQuery.getResource())) {
                    predicates.add(cb.equal(root.get("resource").as(String.class), handleRuleQuery.getResource()));
                }
                if (StringUtils.isNotEmpty(handleRuleQuery.getModuleId())) {
                    predicates.add(cb.equal(root.get("moduleId").as(String.class), handleRuleQuery.getModuleId()));
                }
                if (StringUtils.isNotEmpty(handleRuleQuery.getHandleOption())) {
                    predicates.add(cb.equal(root.get("handleOption").as(String.class), handleRuleQuery.getHandleOption()));
                }
                if (Objects.nonNull(DelFlag.getDel(handleRuleQuery.getDelFlag()))) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), handleRuleQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
