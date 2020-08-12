package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.HandleActionMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.HandleActionDO;
import cn.springcloud.gray.server.dao.repository.HandleActionRepository;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.domain.query.HandleActionQuery;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 10:03
 */
@Service
public class HandleActionService extends AbstraceCRUDService<HandleAction, HandleActionRepository, HandleActionDO, Long> {

    @Autowired
    private HandleActionRepository handleActionRepository;
    @Autowired
    private HandleActionMapper handleActionMapper;


    @Override
    protected HandleActionRepository getRepository() {
        return handleActionRepository;
    }

    @Override
    protected ModelMapper<HandleAction, HandleActionDO> getModelMapper() {
        return handleActionMapper;
    }

    public List<HandleAction> findAllModelsByHandleId(Long handleId) {
        return handleActionMapper.dos2models(handleActionRepository.findAllByHandleId(handleId));
    }

    public List<HandleAction> findAllModelsByHandleId(Long handleId, boolean delFlag) {
        return handleActionMapper.dos2models(handleActionRepository.findAllByHandleIdAndDelFlag(handleId, delFlag));
    }

    public Page<HandleAction> findAllModelsByHandleId(Long handleId, Pageable pageable) {
        Page<HandleActionDO> page = handleActionRepository.findAllByHandleId(handleId, pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());
    }

    public Page<HandleAction> listHandleActions(HandleActionQuery query, Pageable pageable) {
        Page<HandleActionDO> page = handleActionRepository.findAll(createSpecification(query), pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());
    }

    private Specification<HandleActionDO> createSpecification(HandleActionQuery handleActionQuery) {
        return new Specification<HandleActionDO>() {

            @Override
            public Predicate toPredicate(Root<HandleActionDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList();

                if (Objects.isNull(handleActionQuery.getHandleId())) {
                    predicates.add(cb.equal(root.get("handleId").as(Long.class), handleActionQuery.getHandleId()));
                }
                if (Objects.nonNull(handleActionQuery.getDelFlag()) && !Objects.equals(handleActionQuery.getDelFlag(), DelFlag.ALL)) {
                    predicates.add(cb.equal(root.get("delFlag").as(Boolean.class), handleActionQuery.getDelFlag().getDel()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
