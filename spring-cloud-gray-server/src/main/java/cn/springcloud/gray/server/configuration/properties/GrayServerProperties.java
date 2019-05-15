package cn.springcloud.gray.server.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gray.server")
public class GrayServerProperties {

    private int evictionIntervalTimerInMs = 60000;

    public int getEvictionIntervalTimerInMs() {
        return evictionIntervalTimerInMs;
    }
}
