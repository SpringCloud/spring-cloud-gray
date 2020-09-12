package cn.springcloud.gray.client.dubbo.configuration;

import cn.springcloud.gray.client.dubbo.servernode.NacosServiceInstanceIdExtractor;
import cn.springcloud.gray.client.dubbo.servernode.ServiceInstanceIdExtractor;
import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-09-11 10:36
 */
@Configuration
@ConditionalOnNacosDiscoveryEnabled
public class DubboPluginNacosRegistoryConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceInstanceIdExtractor serviceInstanceIdExtractor() {
        return new NacosServiceInstanceIdExtractor();
    }
}
