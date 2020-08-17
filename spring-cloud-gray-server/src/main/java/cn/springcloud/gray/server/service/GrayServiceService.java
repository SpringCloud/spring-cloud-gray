package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayServiceMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayServiceDO;
import cn.springcloud.gray.server.dao.model.UserServiceAuthorityDO;
import cn.springcloud.gray.server.dao.repository.GrayServiceRepository;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.gray.domain.query.GrayServiceQuery;
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

@Service
public class GrayServiceService extends AbstraceCRUDService<GrayService, GrayServiceRepository, GrayServiceDO, String> {

    @Autowired
    private GrayServiceRepository repository;
    @Autowired
    private GrayInstanceService grayInstanceService;
    @Autowired
    private GrayServiceMapper grayServiceMapper;


    @Override
    protected GrayServiceRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayService, GrayServiceDO> getModelMapper() {
        return grayServiceMapper;
    }

    public void deleteById(String id) {
        delete(id);
        grayInstanceService.findByServiceId(id);
    }

    public void deleteReactById(String id) {
        delete(id);
        grayInstanceService.findByServiceId(id).forEach(entity -> {
            grayInstanceService.deleteReactById(entity.getInstanceId());
        });
    }


    public Page<GrayService> listAllGrayServices(Pageable pageable) {
        Page<GrayServiceDO> entities = repository.findAll(pageable);
        return PaginationUtils.convert(pageable, entities, grayServiceMapper);
    }

    public Page<GrayService> queryGrayServices(GrayServiceQuery serviceQuery, Pageable pageable) {
        Specification<GrayServiceDO> specification = new Specification<GrayServiceDO>() {

            @Override
            public Predicate toPredicate(Root<GrayServiceDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (StringUtils.isNotEmpty(serviceQuery.getNamespace())) {
                    predicates.add(cb.equal(root.get("namespace").as(String.class), serviceQuery.getNamespace()));
                }
                if (StringUtils.isNotEmpty(serviceQuery.getUserId())) {
                    Subquery subQuery = query.subquery(String.class);
//                    Root from = subQuery.from(UserResourceAuthorityDO.class);
//
//                    subQuery.select(from.get("resourceId").as(String.class))
//                            .where(cb.equal(from.get("userId").as(String.class), serviceQuery.getUserId()),
//                                    cb.equal(from.get("resource").as(String.class), AuthorityConstants.RESOURCE_GRAY_SERVICE));
//
//                    predicates.add(cb.and((root.get("serviceId")).in(subQuery)));


                    Root from = subQuery.from(UserServiceAuthorityDO.class);
                    subQuery.select(from.get("serviceId").as(String.class))
                            .where(cb.equal(from.get("userId").as(String.class), serviceQuery.getUserId()));
                    predicates.add(cb.and((root.get("serviceId")).in(subQuery)));

                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Page<GrayServiceDO> page = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, page, grayServiceMapper);
    }
}
