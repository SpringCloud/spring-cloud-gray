package cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author saleson
 * @date 2020-02-04 19:55
 */
@ConfigurationProperties("gray.client.event.longpolling")
@Data
public class LongPollingProperties {

    private long timeout = 15000;

}
