package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.HandleRuleDefinition;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.event.HandleRuleEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;
import cn.springcloud.gray.event.server.TriggerType;

/**
 * @author saleson
 * @date 2020-05-31 15:34
 */
public class HandleRuleEventConverter extends AbstrctEventConverter<HandleRule, HandleRuleEvent> {

    private GrayModule grayModule;

    public HandleRuleEventConverter(GrayModule grayModule) {
        this.grayModule = grayModule;
    }

    @Override
    protected HandleRuleEvent convertDeleteData(HandleRule handleRule) {
        return toEvent(TriggerType.DELETE, handleRule);
    }

    @Override
    protected HandleRuleEvent convertModifyData(HandleRule handleRule) {
        return toEvent(TriggerType.ADD, handleRule);
    }

    private HandleRuleEvent toEvent(TriggerType triggerType, HandleRule handleRule) {
        HandleRuleEvent event = new HandleRuleEvent();
        event.setModuleId(handleRule.getModuleId());
        event.setResource(handleRule.getResource());
        HandleRuleDefinition definition = grayModule.toHandleRuleDefinition(handleRule);
        event.setHandleRuleDefinition(definition);
        event.setTriggerType(triggerType);
        return event;
    }
}
