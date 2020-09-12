package cn.springcloud.gray.client.dubbo.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.dubbo.DubboRequestInterceptor;
import cn.springcloud.gray.client.dubbo.GrayDubboInitializer;
import cn.springcloud.gray.client.dubbo.configuration.properties.GrayDubboProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-09-11 00:29
 */
@Configuration
@ConditionalOnBean(GrayManager.class)
@EnableConfigurationProperties(GrayDubboProperties.class)
public class DubboPluginAutoConfiguration {

//    @Bean
//    public GrayRouterFactory grayRouterFactory(ServerDistinguisher serverDistinguisher) {
//        return new GrayRouterFactory(serverDistinguisher);
//    }

    @Bean
    public GrayDubboInitializer grayDubboInitializer() {
        return new GrayDubboInitializer();
    }

    @Bean
    public DubboRequestInterceptor dubboRequestInterceptor() {
        return new DubboRequestInterceptor();
    }

}
