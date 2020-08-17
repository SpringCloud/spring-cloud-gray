package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.InstanceRoutePolicyMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.RoutePolicyRecordDO;
import cn.springcloud.gray.server.dao.repository.RoutePolicyRecordRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.server.module.route.policy.domain.query.RoutePolicyQuery;
import cn.springcloud.gray.server.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RoutePolicyRecordService extends AbstraceCRUDService<RoutePolicyRecord, RoutePolicyRecordRepository, RoutePolicyRecordDO, Long> {
    @Autowired
    private RoutePolicyRecordRepository repository;
    @Autowired
    private InstanceRoutePolicyMapper mapper;

    @Override
    protected RoutePolicyRecordRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<RoutePolicyRecord, RoutePolicyRecordDO> getModelMapper() {
        return mapper;
    }

    public Page<RoutePolicyRecord> queryRoutePolicies(RoutePolicyQuery irpQuery, Pageable pageable) {
        Specification<RoutePolicyRecordDO> specification = createSpecification(irpQuery);
        Page<RoutePolicyRecordDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, mapper);
    }

    public List<RoutePolicyRecord> queryRoutePolicies(RoutePolicyQuery irpQuery) {
        Specification<RoutePolicyRecordDO> specification = createSpecification(irpQuery);
        return dos2models(repository.findAll(specification));
    }


    public RoutePolicyRecord findFirstAscByDelFlag(RoutePolicyQuery irpQuery) {
        Specification<RoutePolicyRecordDO> specification = createSpecification(irpQuery);
        Pageable pageable = new PageRequest(
                0, 1, new Sort(new Sort.Order(Sort.Direction.ASC, "delFlag")));
        Page<RoutePolicyRecordDO> page = repository.findAll(specification, pageable);
        if (!page.hasContent()) {
            return null;
        }
        return do2model(page.getContent().get(0));
    }

    public RoutePolicyRecord find(String type, String moduleId, String resource, Long policyId) {
        return getModelMapper().do2model(
                repository.findFirstByTypeAndModuleIdAndResourceAndPolicyIdOrderByIdDesc(type, moduleId, resource, policyId));
    }


    public Specification<RoutePolicyRecordDO> createSpecification(RoutePolicyQuery irpQuery) {
        return new Specification<RoutePolicyRecordDO>() {
            @Override
            public Predicate toPredicate(Root<RoutePolicyRecordDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(irpQuery.getType())) {
                    predicates.add(cb.equal(root.get("type").as(String.class), irpQuery.getType()));
                }
                if (StringUtils.isNotEmpty(irpQuery.getModuleId())) {
                    predicates.add(cb.equal(root.get("moduleId").as(String.class), irpQuery.getModuleId()));
                }
                if (StringUtils.isNotEmpty(irpQuery.getResource())) {
                    predicates.add(cb.equal(root.get("resource").as(String.class), irpQuery.getResource()));
                }
                if (Objects.nonNull(irpQuery.getPolicyId())) {
                    predicates.add(cb.equal(root.get("policyId").as(Long.class), irpQuery.getPolicyId()));
                }
                if (Objects.nonNull(DelFlag.getDel(irpQuery.getDelFlag()))) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), irpQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };
    }
}
