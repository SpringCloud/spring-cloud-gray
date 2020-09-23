package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.event.GrayEvent;
import org.apache.commons.lang3.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-08-13 23:18
 */
public class SimpleEventTypeRegistry implements EventTypeRegistry {

    private volatile Map<String, Class<? extends GrayEvent>> typeClsMappings = new HashMap<>();

    @Override
    public Class<? extends GrayEvent> lookup(String eventType) {
        return typeClsMappings.get(eventType);
    }

    @Override
    public synchronized void bind(String eventType, Class<? extends GrayEvent> cls) {
        Map<String, Class<? extends GrayEvent>> mappings = new HashMap<>(this.typeClsMappings);
        mappings.put(eventType, cls);
        this.typeClsMappings = mappings;
    }

    @Override
    public void bind(String eventType, String eventClass) throws ClassNotFoundException {
        bind(eventType, parseEventClass(eventClass));
    }

    @Override
    public void bind(String eventClass) throws ClassNotFoundException {
        bind(eventClass, parseEventClass(eventClass));
    }

    private Class<? extends GrayEvent> parseEventClass(String eventClass) throws ClassNotFoundException {
        Class<?> cls = ClassUtils.getClass(eventClass);
        if (!GrayEvent.class.isAssignableFrom(cls)) {
            throw new ClassCastException(eventClass + " can not cast to " + GrayEvent.class);
        }
        return (Class<? extends GrayEvent>) cls;
    }

}
