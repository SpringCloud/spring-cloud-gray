package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.constant.AuthorityConstants;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.mapper.NamespaceMapper;
import cn.springcloud.gray.server.dao.model.DefaultNamespaceDO;
import cn.springcloud.gray.server.dao.model.NamespaceDO;
import cn.springcloud.gray.server.dao.model.UserResourceAuthorityDO;
import cn.springcloud.gray.server.dao.repository.DefaultNamespaceRepository;
import cn.springcloud.gray.server.dao.repository.NamespaceRepository;
import cn.springcloud.gray.server.module.domain.Namespace;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NamespaceService extends AbstraceCRUDService<Namespace, NamespaceRepository, NamespaceDO, String> {

    @Autowired
    private NamespaceRepository repository;
    @Autowired
    private NamespaceMapper mapper;
    @Autowired
    private DefaultNamespaceRepository defaultNamespaceRepository;


    @Override
    protected NamespaceRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<Namespace, NamespaceDO> getModelMapper() {
        return mapper;
    }

    public Page<Namespace> findAllByUser(String userId, Pageable pageable) {
        Specification<NamespaceDO> specification = createSpecificationByUserId(userId);
        Page<NamespaceDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, mapper);
    }

    public List<Namespace> findAllByUser(String userId) {
        Specification<NamespaceDO> specification = createSpecificationByUserId(userId);
        List<NamespaceDO> list = repository.findAll(specification);
        return dos2models(list);
    }


    public boolean setDefaultNamespace(String userId, String nsCode) {
        DefaultNamespaceDO defaultNamespaceDO = new DefaultNamespaceDO();
        defaultNamespaceDO.setNsCode(nsCode);
        defaultNamespaceDO.setUserId(userId);
        defaultNamespaceRepository.save(defaultNamespaceDO);
        return true;
    }

    public String getDefaultNamespace(String userId) {
        DefaultNamespaceDO defaultNamespaceDO = defaultNamespaceRepository.findOne(userId);
        return Objects.nonNull(defaultNamespaceDO) ? defaultNamespaceDO.getNsCode() : null;
    }


    private Specification<NamespaceDO> createSpecificationByUserId(String userId) {
        return new Specification<NamespaceDO>() {
            @Override
            public Predicate toPredicate(Root<NamespaceDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();
                if (StringUtils.isNotEmpty(userId)) {
                    Subquery subQuery = query.subquery(String.class);
                    Root from = subQuery.from(UserResourceAuthorityDO.class);

                    subQuery.select(from.get("resourceId").as(String.class))
                            .where(cb.equal(from.get("userId").as(String.class), userId),
                                    cb.equal(from.get("resource").as(String.class), AuthorityConstants.RESOURCE_NAMESPACE),
                                    cb.equal(from.get("delFlag").as(Boolean.class), false)
                            );

                    predicates.add(cb.and((root.get("code")).in(subQuery)));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };
    }
}
