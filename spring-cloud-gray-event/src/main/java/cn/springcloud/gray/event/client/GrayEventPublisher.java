package cn.springcloud.gray.event.client;

import cn.springcloud.gray.event.GrayEvent;

/**
 * 事件发布器
 * 调用事件监听器
 *
 * @author saleson
 * @date 2020-01-30 12:48
 */
public interface GrayEventPublisher {


    void publishEvent(GrayEvent grayEvent);

}
