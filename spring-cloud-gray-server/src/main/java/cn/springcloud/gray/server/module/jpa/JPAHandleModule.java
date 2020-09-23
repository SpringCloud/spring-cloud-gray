package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.server.module.HandleModule;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.domain.HandleDetailInfos;
import cn.springcloud.gray.server.module.domain.query.HandleActionQuery;
import cn.springcloud.gray.server.module.domain.query.HandleQuery;
import cn.springcloud.gray.server.service.HandleActionService;
import cn.springcloud.gray.server.service.HandleService;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.event.server.TriggerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 10:01
 */
public class JPAHandleModule implements HandleModule {

    private HandleService handleService;
    private HandleActionService handleActionService;
    private GrayEventTrigger grayEventTrigger;


    public JPAHandleModule(HandleService handleService, HandleActionService handleActionService, GrayEventTrigger grayEventTrigger) {
        this.handleService = handleService;
        this.handleActionService = handleActionService;
        this.grayEventTrigger = grayEventTrigger;
    }

    @Override
    public Page<Handle> queryHandles(HandleQuery handleQuery, Pageable pageable) {
        return handleService.findAllModels(handleQuery, pageable);
    }

    @Override
    public List<Handle> queryHandles(HandleQuery handleQuery) {
        return handleService.findAllModels(handleQuery);
    }

    @Override
    public List<Handle> findAllEnabledHandles() {
        return findHandles(HandleQuery.builder().delFlag(DelFlag.UNDELETE).build());
    }

    @Override
    public List<Handle> findHandles(HandleQuery handleQuery) {
        return handleService.findAllModels(handleQuery);
    }


    @Override
    public Handle getHandle(Long handleId) {
        return handleService.findOneModel(handleId);
    }

    @Override
    public Handle saveHandle(Handle handle) {
        boolean triggerEvent = false;
        Handle old = null;
        if (Objects.isNull(handle.getId())) {
            if (Objects.isNull(handle.getDelFlag())) {
                handle.setDelFlag(false);
            }
            triggerEvent = true;
        } else {
            old = getHandle(handle.getId());
            if (Objects.isNull(old) || Objects.equals(old.getDelFlag(), Boolean.FALSE)) {
                triggerEvent = true;
            }
        }
        if (Objects.isNull(handle.getOperateTime())) {
            handle.setOperateTime(new Date());
        }

        handle = handleService.saveModel(handle);
        if (triggerEvent) {
            triggerGrayEvent(Objects.isNull(old) ? TriggerType.ADD : TriggerType.MODIFY, handle);
        }
        return handle;
    }

    @Override
    public boolean deleteHandle(Long handleId, String userId) {
        int result = updateHandleDelFlag(handleId, true, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.DELETE, handleService.findOneModel(handleId));
        }
        return result < 0;
    }

    @Override
    public boolean recoverHandle(Long handleId, String userId) {
        int result = updateHandleDelFlag(handleId, false, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.ADD, handleService.findOneModel(handleId));
        }
        return result < 0;
    }

    /**
     * 更新Handle删除标记
     *
     * @param handleId
     * @param newDelFlag
     * @param userId
     * @return 0: 没有找到记录， 1: 新标记与记录中的一致, 2: 更新成功
     */
    protected int updateHandleDelFlag(Long handleId, Boolean newDelFlag, String userId) {
        Handle record = handleService.findOneModel(handleId);
        if (Objects.isNull(record)) {
            return 0;
        }
        if (Objects.equals(record.getDelFlag(), newDelFlag)) {
            return 1;
        }
        record.setDelFlag(newDelFlag);
        record.setOperateTime(new Date());
        record.setOperator(userId);
        handleService.saveModel(record);
        return 2;
    }

    @Override
    public HandleAction saveHandleAction(HandleAction handleAction) {
        boolean triggerEvent = false;
        HandleAction old = null;
        if (Objects.isNull(handleAction.getId())) {
            if (Objects.isNull(handleAction.getDelFlag())) {
                handleAction.setDelFlag(false);
            }
            triggerEvent = true;
        } else {
            old = getHandleAction(handleAction.getId());
            if (Objects.isNull(old) || Objects.equals(old.getDelFlag(), Boolean.FALSE)) {
                triggerEvent = true;
            }
        }
        if (Objects.isNull(handleAction.getOperateTime())) {
            handleAction.setOperateTime(new Date());
        }

        handleAction = handleActionService.saveModel(handleAction);
        if (triggerEvent) {
            triggerGrayEvent(Objects.isNull(old) ? TriggerType.ADD : TriggerType.MODIFY, handleAction);
        }
        return handleAction;
    }

    @Override
    public boolean deleteHandleAction(Long handleActionId, String userId) {
        int result = updateHandleActionDelFlag(handleActionId, true, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.DELETE, handleActionService.findOneModel(handleActionId));
        }
        return result < 0;
    }

    @Override
    public boolean recoverHandleAction(Long handleActionId, String userId) {
        int result = updateHandleActionDelFlag(handleActionId, false, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.ADD, handleActionService.findOneModel(handleActionId));
        }
        return result < 0;
    }


    /**
     * 更新HandleAction删除标记
     *
     * @param handleActionId
     * @param newDelFlag
     * @param userId
     * @return 0: 没有找到记录， 1: 新标记与记录中的一致, 2: 更新成功
     */
    protected int updateHandleActionDelFlag(Long handleActionId, Boolean newDelFlag, String userId) {
        HandleAction record = handleActionService.findOneModel(handleActionId);
        if (Objects.isNull(record)) {
            return 0;
        }
        if (Objects.equals(record.getDelFlag(), newDelFlag)) {
            return 1;
        }
        record.setDelFlag(newDelFlag);
        record.setOperateTime(new Date());
        record.setOperator(userId);
        handleActionService.saveModel(record);
        return 2;
    }

    @Override
    public HandleAction getHandleAction(Long id) {
        return handleActionService.findOneModel(id);
    }

    @Override
    public List<HandleAction> listEnabledHandleActionsByHandleId(Long handleId) {
        return handleActionService.findAllModelsByHandleId(handleId, false);
    }

    @Override
    public List<HandleAction> listHandleActionsByHandleId(Long handleId) {
        return handleActionService.findAllModelsByHandleId(handleId);
    }

    @Override
    public Page<HandleAction> listHandleActionsByHandleId(Long handleId, Pageable pageable) {
        return handleActionService.findAllModelsByHandleId(handleId, pageable);
    }

    @Override
    public Page<HandleAction> listHandleActions(HandleActionQuery query, Pageable pageable) {
        return handleActionService.listHandleActions(query, pageable);
    }

    @Override
    public HandleDetailInfos newHandle(HandleDetailInfos handleDetailInfos) {
        //todo
        return null;
    }

    protected void triggerGrayEvent(TriggerType triggerType, Object source) {
        grayEventTrigger.triggering(source, triggerType);
    }
}
