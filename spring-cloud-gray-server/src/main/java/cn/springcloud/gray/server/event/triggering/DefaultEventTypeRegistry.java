package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.server.configuration.properties.GrayServerEventProperties;

import java.util.List;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-08-13 23:42
 */
public class DefaultEventTypeRegistry extends SimpleEventTypeRegistry {

    private GrayServerEventProperties grayServerEventProperties;

    public DefaultEventTypeRegistry(GrayServerEventProperties grayServerEventProperties) throws ClassNotFoundException {
        this.grayServerEventProperties = grayServerEventProperties;
        initEventTypeMappings();
    }

    protected void initEventTypeMappings() throws ClassNotFoundException {
        bindEventClasses();
        bindEventMappings();
    }

    private void bindEventClasses() throws ClassNotFoundException {
        List<String> eventClesses = grayServerEventProperties.getEventTypeMapping().getClesses();
        for (String eventCless : eventClesses) {
            bind(eventCless);
        }
    }

    private void bindEventMappings() throws ClassNotFoundException {
        Map<String, String> mappings = grayServerEventProperties.getEventTypeMapping().getMappings();
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            this.bind(entry.getKey(), entry.getValue());
        }
    }
}
