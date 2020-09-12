package cn.springcloud.gray.client.dubbo.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-09-12 23:43
 */
@Data
@ConfigurationProperties(prefix = "gray.dubbo")
public class GrayDubboProperties {

    private Set<String> ignoreGrayServiceNames = new HashSet<>();
}
