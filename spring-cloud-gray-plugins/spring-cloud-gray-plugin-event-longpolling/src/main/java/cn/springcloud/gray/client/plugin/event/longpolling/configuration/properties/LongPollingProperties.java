package cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties;

import cn.springcloud.gray.bean.properties.ConfigurationProperties;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-04 19:55
 */
@ConfigurationProperties("gray.client.event.longpolling")
@Data
public class LongPollingProperties {

    private long timeout = 15000;

}
