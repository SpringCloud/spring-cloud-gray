package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.event.GraySourceEventPublisher;
import cn.springcloud.gray.exceptions.EventException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultGraySourceEventPublisher implements GraySourceEventPublisher {

    private GrayEventPublisher publisherDelegater;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    private EventSourceConvertService eventSourceConvertService;


    public DefaultGraySourceEventPublisher(
            GrayEventPublisher publisherDelegater,
            ExecutorService executorService,
            EventSourceConvertService eventSourceConvertService) {
        this.publisherDelegater = publisherDelegater;
        this.executorService = executorService;
        this.eventSourceConvertService = eventSourceConvertService;
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void publishEvent(GrayEventMsg msg, Object source) throws EventException {
        Object eventSource = getEventMsgSource(msg, source);
        msg.setSource(eventSource);
        publishEvent(msg);
    }

    @Override
    public void publishEvent(GrayEventMsg msg, Object source, long delayTimeMs) throws EventException {
        scheduledExecutorService.schedule(()-> publishEvent(msg, source), delayTimeMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void asyncPublishEvent(GrayEventMsg msg, Object source) throws EventException {
        executorService.submit(()->publishEvent(msg, source));
    }

    @Override
    public void asyncPublishEvent(GrayEventMsg msg, Object source, long delayTimeMs) throws EventException {
        executorService.submit(()->publishEvent(msg, source, delayTimeMs));
    }

    @Override
    public void publishEvent(GrayEventMsg msg) throws EventException {
        publisherDelegater.publishEvent(msg);
    }

    private Object getEventMsgSource(GrayEventMsg msg, Object source) {
        return eventSourceConvertService.convert(msg.getEventType(), msg.getSourceType(), source);
    }


}
