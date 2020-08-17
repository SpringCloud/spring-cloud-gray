package cn.springcloud.gray.server.configuration.properties;

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

    @Data
    public static class EventTypeMapping {
        private List<String> clesses = new ArrayList<>();

        private Map<String, String> mappings = new HashMap<>();
    }
}
