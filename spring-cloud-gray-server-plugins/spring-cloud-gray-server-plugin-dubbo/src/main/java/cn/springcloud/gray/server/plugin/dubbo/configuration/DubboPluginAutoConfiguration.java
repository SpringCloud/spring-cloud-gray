package cn.springcloud.gray.server.plugin.dubbo.configuration;

import cn.springcloud.gray.server.plugin.dubbo.discovery.DubboNacosInstanceInfoAnalyser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-09-09 11:21
 */
@Configuration
public class DubboPluginAutoConfiguration {

    @Bean
    @ConditionalOnBean(name = "com.alibaba.cloud.nacos.NacosDiscoveryProperties")
    public DubboNacosInstanceInfoAnalyser dubboNacosInstanceInfoAnalyser() {
        return new DubboNacosInstanceInfoAnalyser();
    }
}
