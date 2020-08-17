package cn.springcloud.gray.client.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author saleson
 * @date 2020-05-27 10:52
 */
@ConfigurationProperties("gray.mock")
public class GrayMockProperties {
    private boolean enabled;
}
