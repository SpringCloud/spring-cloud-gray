package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.model.GrayTrackDefinition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties("gray.load")
@Setter
@Getter
public class GrayLoadProperties {
    private boolean enabled = false;
    private Map<String, GrayServiceProperties> services = new HashMap<>();
    private List<GrayTrackDefinition> trackDefinitions = new ArrayList<>();

    @Data
    public class GrayServiceProperties {
        private List<GrayInstanceProperties> instances;
    }

    @Data
    public class GrayInstanceProperties {
        private String instanceId;
        private String host;
        private Integer port;
        private Set<String> routePolicies = new HashSet<>();
    }

}
