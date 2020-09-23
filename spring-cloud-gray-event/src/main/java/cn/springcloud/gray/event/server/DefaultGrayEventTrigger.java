package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author saleson
 * @date 2020-01-31 17:16
 */
public class DefaultGrayEventTrigger extends AbstractGrayEventTrigger {

    private GrayEventLogger grayEventLogger;

    public DefaultGrayEventTrigger(
            GrayEventSender grayEventSender,
            GrayEventLogger grayEventLogger,
            ExecutorService executorService) {
        this(grayEventSender, null, grayEventLogger, executorService);
    }

    public DefaultGrayEventTrigger(
            GrayEventSender grayEventSender,
            List<EventConverter> eventConverters,
            GrayEventLogger grayEventLogger,
            ExecutorService executorService) {
        super(grayEventSender, eventConverters, executorService);
        this.grayEventLogger = grayEventLogger;
    }

    @Override
    protected void logEventTrigger(Object eventSource, TriggerType triggerType, GrayEvent grayEvent) {
        grayEventLogger.log(eventSource, triggerType, grayEvent);
    }
}
