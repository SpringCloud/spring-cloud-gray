package cn.springcloud.gray.client.netflix.configuration.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("gray.hystrix")
public class GrayHystrixProperties {
    private boolean enabled;

    private ThreadTransmitStrategy threadTransmitStrategy = ThreadTransmitStrategy.WRAP_CALLABLE;

    public static enum ThreadTransmitStrategy {
        WRAP_CALLABLE, HYSTRIX_REQUEST_LOCAL_STORAGE
    }
}
