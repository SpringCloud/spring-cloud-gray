package cn.springcloud.gray.event;

import cn.springcloud.gray.exceptions.EventException;

public interface GraySourceEventPublisher extends GrayEventPublisher {

    void publishEvent(GrayEventMsg msg, Object source) throws EventException;

    void publishEvent(GrayEventMsg msg, Object source, long delayTimeMs) throws EventException;

    void asyncPublishEvent(GrayEventMsg msg, Object source) throws EventException;

    void asyncPublishEvent(GrayEventMsg msg, Object source, long delayTimeMs) throws EventException;
}
