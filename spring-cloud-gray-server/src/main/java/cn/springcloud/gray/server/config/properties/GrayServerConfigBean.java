package cn.springcloud.gray.server.config.properties;

import cn.springcloud.gray.server.GrayServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gray.server")
public class GrayServerConfigBean implements GrayServerConfig {

    private int evictionIntervalTimerInMs = 60000;

    @Override
    public int getEvictionIntervalTimerInMs() {
        return evictionIntervalTimerInMs;
    }
}
