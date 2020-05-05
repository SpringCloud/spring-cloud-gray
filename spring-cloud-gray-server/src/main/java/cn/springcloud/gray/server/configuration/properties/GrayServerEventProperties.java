package cn.springcloud.gray.server.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

}
