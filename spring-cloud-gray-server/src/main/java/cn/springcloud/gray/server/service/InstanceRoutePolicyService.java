package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.InstanceRoutePolicyMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayInstanceRoutePolicyDO;
import cn.springcloud.gray.server.dao.repository.GrayInstanceRoutePolicyRepository;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springcloud.gray.server.module.gray.domain.query.InstanceRoutePolicyQuery;
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

@Service
public class InstanceRoutePolicyService extends AbstraceCRUDService<InstanceRoutePolicy, GrayInstanceRoutePolicyRepository, GrayInstanceRoutePolicyDO, String> {
    @Autowired
    private GrayInstanceRoutePolicyRepository repository;
    @Autowired
    private InstanceRoutePolicyMapper mapper;

    @Override
    protected GrayInstanceRoutePolicyRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<InstanceRoutePolicy, GrayInstanceRoutePolicyDO> getModelMapper() {
        return mapper;
    }

    public Page<InstanceRoutePolicy> queryInstanceRoutePolicies(InstanceRoutePolicyQuery irpQuery, Pageable pageable) {
        Specification<GrayInstanceRoutePolicyDO> specification = new Specification<GrayInstanceRoutePolicyDO>() {
            @Override
            public Predicate toPredicate(Root<GrayInstanceRoutePolicyDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(irpQuery.getInstanceId())) {
                    predicates.add(cb.equal(root.get("instanceId").as(String.class), irpQuery.getInstanceId()));
                }
                if (Objects.isNull(irpQuery.getPolicyId())) {
                    predicates.add(cb.equal(root.get("policyId").as(Long.class), irpQuery.getPolicyId()));
                }
                if (Objects.isNull(irpQuery.getDelFlag())) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), irpQuery.getDelFlag()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };

        Page<GrayInstanceRoutePolicyDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, mapper);
    }


    public List<InstanceRoutePolicy> findAllRoutePoliciesByInstanceId(String instanceId, Boolean delFlag) {
        Specification<GrayInstanceRoutePolicyDO> specification = new Specification<GrayInstanceRoutePolicyDO>() {
            @Override
            public Predicate toPredicate(Root<GrayInstanceRoutePolicyDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();
                predicates.add(cb.equal(root.get("instanceId").as(String.class), instanceId));

                if (!Objects.isNull(delFlag)) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), delFlag));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };

        List<GrayInstanceRoutePolicyDO> records = repository.findAll(specification);
        return mapper.dos2models(records);
    }
}
