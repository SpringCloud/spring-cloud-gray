package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.HandleActionDefinition;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.event.HandleActionEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;
import cn.springcloud.gray.event.server.TriggerType;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 15:34
 */
public class HandleActionEventConverter extends AbstrctEventConverter<HandleAction, HandleActionEvent> {

    private GrayModule grayModule;

    public HandleActionEventConverter(GrayModule grayModule) {
        this.grayModule = grayModule;
    }

    @Override
    protected HandleActionEvent convertDeleteData(HandleAction handleAction) {
        return toEvent(TriggerType.DELETE, handleAction);
    }

    @Override
    protected HandleActionEvent convertModifyData(HandleAction handleAction) {
        return toEvent(TriggerType.ADD, handleAction);
    }

    private HandleActionEvent toEvent(TriggerType triggerType, HandleAction handleAction) {
        HandleActionEvent event = new HandleActionEvent();
        HandleActionDefinition definition = grayModule.toHandleActionDefinition(handleAction);
        if (Objects.isNull(definition)) {
            return null;
        }
        definition.setId(String.valueOf(handleAction.getId()));
        event.setHandleActionDefinition(definition);
        event.setHandleId(String.valueOf(handleAction.getHandleId()));
        event.setTriggerType(triggerType);
        return event;
    }
}
