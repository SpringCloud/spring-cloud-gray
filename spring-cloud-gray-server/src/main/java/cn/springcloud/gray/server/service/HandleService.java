package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.HandleMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.HandleDO;
import cn.springcloud.gray.server.dao.repository.HandleRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.server.module.domain.query.HandleQuery;
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
public class HandleService extends AbstraceCRUDService<Handle, HandleRepository, HandleDO, Long> {

    @Autowired
    private HandleRepository handleRepository;
    @Autowired
    private HandleMapper handleMapper;

    @Override
    protected HandleRepository getRepository() {
        return handleRepository;
    }

    @Override
    protected ModelMapper<Handle, HandleDO> getModelMapper() {
        return handleMapper;
    }


    public Page<Handle> findAllModels(HandleQuery handleQuery, Pageable pageable) {
        Page<HandleDO> page = handleRepository.findAll(createSpecification(handleQuery), pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());
    }

    public List<Handle> findAllModels(HandleQuery handleQuery) {
        return getModelMapper().dos2models(handleRepository.findAll(createSpecification(handleQuery)));
    }


    private Specification<HandleDO> createSpecification(HandleQuery handleQuery) {
        return new Specification<HandleDO>() {

            @Override
            public Predicate toPredicate(Root<HandleDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(handleQuery.getName())) {
                    predicates.add(cb.equal(root.get("name").as(String.class), handleQuery.getName()));
                }
                if (StringUtils.isNotEmpty(handleQuery.getNamespace())) {
                    predicates.add(cb.equal(root.get("namespace").as(String.class), handleQuery.getNamespace()));
                }
                if (StringUtils.isNotEmpty(handleQuery.getType())) {
                    predicates.add(cb.equal(root.get("type").as(String.class), handleQuery.getType()));
                }
                if (Objects.nonNull(DelFlag.getDel(handleQuery.getDelFlag()))) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), handleQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
