package cn.springcloud.gray.event;

import cn.springcloud.gray.exceptions.EventException;

public interface GrayEventPublisher {

    void publishEvent(GrayEventMsg msg) throws EventException;
}
