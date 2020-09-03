package cn.springcloud.gray.server.plugin.discovery.nacos.configuration;

import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.plugin.discovery.nacos.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayServerPluginNacosDiscoveryAutoConfiguration {

    @Bean
    public ServiceDiscovery serviceDiscovery(NacosDiscoveryProperties discoveryProperties) {
        return new NacosServiceDiscovery(discoveryProperties);
    }
}
