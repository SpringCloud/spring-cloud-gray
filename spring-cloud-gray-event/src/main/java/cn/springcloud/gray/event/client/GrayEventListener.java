package cn.springcloud.gray.event.client;

import cn.springcloud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-30 12:46
 */
public interface GrayEventListener<T extends GrayEvent> {

    void onEvent(T event);
}
