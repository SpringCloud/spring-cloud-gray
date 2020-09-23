package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;

import java.util.List;

/**
 * @author saleson
 * @date 2020-01-31 17:16
 */
public class DefaultGrayEventTrigger extends AbstractGrayEventTrigger {

    private GrayEventLogger grayEventLogger;

    public DefaultGrayEventTrigger(GrayEventSender grayEventSender, GrayEventLogger grayEventLogger) {
        this(grayEventSender, null, grayEventLogger);
    }

    public DefaultGrayEventTrigger(
            GrayEventSender grayEventSender, List<EventConverter> eventConverters, GrayEventLogger grayEventLogger) {
        super(grayEventSender, eventConverters);
        this.grayEventLogger = grayEventLogger;
    }

    @Override
    protected void logEventTrigger(Object eventSource, TriggerType triggerType, GrayEvent grayEvent) {
        grayEventLogger.log(eventSource, triggerType, grayEvent);
    }
}
