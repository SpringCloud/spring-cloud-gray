package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayPolicyMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import cn.springcloud.gray.server.dao.repository.GrayPolicyRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.query.GrayPolicyQuery;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace) {
        List<GrayPolicyDO> entities = repository.findAllByNamespaceAndDelFlag(namespace, false);
        return grayPolicyMapper.dos2models(entities);
    }

    public List<GrayPolicy> findAllModel(Iterable<Long> policyIds, Boolean delFlag) {
        return grayPolicyMapper.dos2models(repository.findAllByIdInAndDelFlag(policyIds, delFlag));
    }

    public List<GrayPolicy> listEnabledGrayPolicies() {
        List<GrayPolicyDO> entities = repository.findAllByDelFlag(false);
        return grayPolicyMapper.dos2models(entities);
    }

    public Page<GrayPolicy> queryGrayPolicies(GrayPolicyQuery query, Pageable pageable) {
        Page<GrayPolicyDO> page = repository.findAll(createSpecification(query), pageable);
        return PaginationUtils.convert(pageable, page, grayPolicyMapper);
    }


    private Specification<GrayPolicyDO> createSpecification(GrayPolicyQuery grayPolicyQuery) {
        return new Specification<GrayPolicyDO>() {

            @Override
            public Predicate toPredicate(Root<GrayPolicyDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (Objects.isNull(grayPolicyQuery.getNamespace())) {
                    predicates.add(cb.equal(root.get("namespace").as(Long.class), grayPolicyQuery.getNamespace()));
                }
                if (Objects.nonNull(grayPolicyQuery.getDelFlag()) && !Objects.equals(grayPolicyQuery.getDelFlag(), DelFlag.ALL)) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), grayPolicyQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
