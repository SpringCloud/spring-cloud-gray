package cn.springcloud.gray.dynamiclogic;

import org.springframework.context.ApplicationEvent;

/**
 * @author saleson
 * @date 2019-12-26 13:08
 */
public class DynamicLogicEvent extends ApplicationEvent {

    private DynamicLogicDefinition dynamicLogicDefinition;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DynamicLogicEvent(DynamicLogicDefinition source) {
        super(source);
    }

    public DynamicLogicDefinition getDynamicLogicDefinition() {
        return dynamicLogicDefinition;
    }
}
