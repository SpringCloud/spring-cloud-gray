package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.mapper.ServiceOwnerMapper;
import cn.springcloud.gray.server.dao.model.ServiceOwnerDO;
import cn.springcloud.gray.server.dao.repository.ServiceOwnerRepository;
import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import cn.springcloud.gray.server.module.user.domain.ServiceOwnerQuery;
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

@Service
public class ServiceOwnerService extends AbstraceCRUDService<ServiceOwner, ServiceOwnerRepository, ServiceOwnerDO, String> {

    @Autowired
    private ServiceOwnerRepository repository;
    @Autowired
    private ServiceOwnerMapper serviceOwnerMapper;

    @Override
    protected ServiceOwnerRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<ServiceOwner, ServiceOwnerDO> getModelMapper() {
        return serviceOwnerMapper;
    }


    public ServiceOwner findServiceOwner(String serviceId) {
        return do2model(getRepository().findByServiceId(serviceId));
    }

    public Page<ServiceOwner> queryServiceOwners(ServiceOwnerQuery serviceOwnerQuery, Pageable pageable) {
//        QServiceOwnerDO qServiceOwnerDO = QServiceOwnerDO.serviceOwnerDO;
//        Predicate predicate = null;
//        switch (queryRecords.getQueryItem()){
//            case ServiceOwnerQuery.QUERY_ITEM_BINDED:
//                predicate = qServiceOwnerDO.userId.isNotNull();
//            case ServiceOwnerQuery.QUERY_ITEM_UNBINDED:
//                predicate = qServiceOwnerDO.userId.isNotNull();
//        }
//        if(StringUtils.isNotEmpty(queryRecords.getServiceId())){
//
//        }

        Specification<ServiceOwnerDO> specification = new Specification<ServiceOwnerDO>() {

            @Override
            public Predicate toPredicate(Root<ServiceOwnerDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList();

                switch (serviceOwnerQuery.getQueryItem()) {
                    case ServiceOwnerQuery.QUERY_ITEM_BINDED:
//                        predicates.add(cb.isNotNull(root.get("userId").as(String.class)));
                        Predicate p1 = cb.isNotNull(root.get("userId"));
                        Predicate p2 = cb.notEqual(root.get("userId").as(String.class), "");
                        predicates.add(cb.and(p1,p2));
                        break;
                    case ServiceOwnerQuery.QUERY_ITEM_UNBINDED:
                        Predicate unbindP1 = cb.isNull(root.get("userId").as(String.class));
                        Predicate unbindP2 = cb.equal(root.get("userId").as(String.class), "");
                        predicates.add(cb.or(unbindP1, unbindP2));
                        break;
                }
                if(StringUtils.isNotEmpty(serviceOwnerQuery.getServiceId())){
                    predicates.add(cb.equal(root.get("serviceId").as(String.class), serviceOwnerQuery.getServiceId()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Page<ServiceOwnerDO> doPage = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, doPage, getModelMapper());
    }
}
