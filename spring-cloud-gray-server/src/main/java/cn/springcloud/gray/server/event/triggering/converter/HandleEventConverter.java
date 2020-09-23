package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.event.HandleEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;
import cn.springcloud.gray.event.server.TriggerType;

/**
 * @author saleson
 * @date 2020-05-31 15:34
 */
public class HandleEventConverter extends AbstrctEventConverter<Handle, HandleEvent> {

    @Override
    protected HandleEvent convertDeleteData(Handle handle) {
        return toHandleEvent(TriggerType.DELETE, handle);
    }

    @Override
    protected HandleEvent convertModifyData(Handle handle) {
        return toHandleEvent(TriggerType.ADD, handle);
    }

    private HandleEvent toHandleEvent(TriggerType triggerType, Handle handle) {
        HandleEvent handleEvent = new HandleEvent();
        handleEvent.setHandleId(String.valueOf(handle.getId()));
        handleEvent.setName(handle.getName());
        handleEvent.setHandleType(handle.getType());
        handleEvent.setTriggerType(triggerType);
        return handleEvent;
    }
}
