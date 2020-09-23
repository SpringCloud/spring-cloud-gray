package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-08-13 23:13
 */
public interface EventTypeRegistry {

    Class<? extends GrayEvent> lookup(String eventType);

    void bind(String eventType, Class<? extends GrayEvent> cls);

    void bind(String eventType, String eventClass) throws ClassNotFoundException;

    void bind(String eventClass) throws ClassNotFoundException;


}
