package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.AuthorityMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.AuthorityDO;
import cn.springcloud.gray.server.dao.repository.AuthorityRepository;
import cn.springcloud.gray.server.module.user.domain.AuthorityDetail;
import cn.springcloud.gray.server.module.user.domain.AuthorityQuery;
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
public class AuthorityService extends AbstraceCRUDService<AuthorityDetail, AuthorityRepository, AuthorityDO, Long> {
    @Autowired
    private AuthorityRepository repository;
    @Autowired
    private AuthorityMapper mapper;

    @Override
    protected AuthorityRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<AuthorityDetail, AuthorityDO> getModelMapper() {
        return mapper;
    }

    public Page<AuthorityDetail> queryAuthorities(AuthorityQuery authorityQuery, Pageable pageable) {
        Specification<AuthorityDO> specification = new Specification<AuthorityDO>() {
            @Override
            public Predicate toPredicate(Root<AuthorityDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(authorityQuery.getRole())) {
                    predicates.add(cb.equal(root.get("role").as(String.class), authorityQuery.getRole()));
                }
                if (Objects.isNull(authorityQuery.getResource())) {
                    predicates.add(cb.equal(root.get("resource").as(Long.class), authorityQuery.getResource()));
                }
                if (Objects.isNull(authorityQuery.getDelFlag())) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), authorityQuery.getDelFlag()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
            }
        };

        Page<AuthorityDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, mapper);
    }

    public AuthorityDetail findAuthorityDetail(String role, String resource) {
        return mapper.do2model(repository.findFirstByRoleAndResource(role, resource));
    }
}
