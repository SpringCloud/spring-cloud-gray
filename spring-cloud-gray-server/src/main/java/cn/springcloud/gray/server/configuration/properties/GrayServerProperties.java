package cn.springcloud.gray.server.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gray.server")
public class GrayServerProperties {

    private int evictionIntervalTimerInMs = 60000;

}
