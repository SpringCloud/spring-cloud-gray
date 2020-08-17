package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.configuration.properties.GrayServerEventProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrayServerEventProperties.class)
public class GrayServerEventAutoConfiguration {


}
