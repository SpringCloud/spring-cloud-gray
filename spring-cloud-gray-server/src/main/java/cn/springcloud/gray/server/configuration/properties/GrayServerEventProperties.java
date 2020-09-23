package cn.springcloud.gray.server.configuration.properties;

import cn.springcloud.gray.concurrent.ConcurrnetProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-05-05 12:29
 */
@Data
@ConfigurationProperties(prefix = "gray.server.event")
public class GrayServerEventProperties {

    /**
     * long-polling, stream
     */
    private String pushType = "long-polling";

    private EventTypeMapping eventTypeMapping = new EventTypeMapping();

    private ConcurrnetProperties triggerThreadPool = new ConcurrnetProperties(5, 20, 30000, 100, "event-trigger");

    private long triggerDelayMills = 20;

    @Data
    public static class EventTypeMapping {
        private List<String> clesses = new ArrayList<>();

        private Map<String, String> mappings = new HashMap<>();
    }
}
