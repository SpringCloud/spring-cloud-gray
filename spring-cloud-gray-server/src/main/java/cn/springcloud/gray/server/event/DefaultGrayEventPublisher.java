package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.exceptions.EventException;

public class DefaultGrayEventPublisher implements GrayEventPublisher {

    @Override
    public void publishEvent(GrayEventMsg msg) throws EventException {

    }
}
