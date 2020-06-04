package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.HandleActionMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.HandleActionDO;
import cn.springcloud.gray.server.dao.repository.HandleActionRepository;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
