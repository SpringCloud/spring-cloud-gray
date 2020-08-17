package cn.springcloud.gray.server.event.longpolling.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author saleson
 * @date 2020-02-03 22:26
 */
@Data
@ConfigurationProperties("gray.server.event.longpolling")
public class EventLongPollingProperties {

    private long defaultTimeoutMs = 60000;


}
