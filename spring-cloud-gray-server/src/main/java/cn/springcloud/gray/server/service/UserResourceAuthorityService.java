package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.mapper.UserResourceAuthorityMapper;
import cn.springcloud.gray.server.dao.model.UserResourceAuthorityDO;
import cn.springcloud.gray.server.dao.repository.UserResourceAuthorityRepository;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthorityQuery;
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
public class UserResourceAuthorityService extends AbstraceCRUDService<UserResourceAuthority, UserResourceAuthorityRepository, UserResourceAuthorityDO, Long> {

    @Autowired
    private UserResourceAuthorityRepository repository;
    @Autowired
    private UserResourceAuthorityMapper mapper;

    @Override
    protected UserResourceAuthorityRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<UserResourceAuthority, UserResourceAuthorityDO> getModelMapper() {
        return mapper;
    }

    public UserResourceAuthority findUserResourceAuthority(String userId, String resource, String resourceId) {
        return mapper.do2model(repository.findFirstByUserIdAndResourceAndResourceId(userId, resource, resourceId));
    }

    public Page<UserResourceAuthority> queryUserResourceAuthority(UserResourceAuthorityQuery uraQuery, Pageable pageable) {
        Specification<UserResourceAuthorityDO> specification = new Specification<UserResourceAuthorityDO>() {
            @Override
            public Predicate toPredicate(Root<UserResourceAuthorityDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();
                if (StringUtils.isNotEmpty(uraQuery.getUserId())) {
                    predicates.add(cb.equal(root.get("userId").as(String.class), uraQuery.getUserId()));
                }
                if (Objects.isNull(uraQuery.getResource())) {
                    predicates.add(cb.equal(root.get("resource").as(Long.class), uraQuery.getResource()));
                }
                if (Objects.isNull(uraQuery.getResourceId())) {
                    predicates.add(cb.equal(root.get("resourceId").as(Long.class), uraQuery.getResourceId()));
                }
                if (Objects.isNull(uraQuery.getAuthorityFlag())) {
                    predicates.add(cb.equal(root.get("authorityFlag").as(Integer.class), uraQuery.getAuthorityFlag().getFlag()));
                }
                if (Objects.isNull(uraQuery.getDelFlag())) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), uraQuery.getDelFlag()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };

        Page<UserResourceAuthorityDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, mapper);
    }
}
