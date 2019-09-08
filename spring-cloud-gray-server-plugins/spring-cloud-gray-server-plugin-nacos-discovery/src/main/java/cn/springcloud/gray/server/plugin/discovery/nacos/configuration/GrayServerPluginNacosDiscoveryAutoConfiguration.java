package cn.springcloud.gray.server.plugin.discovery.nacos.configuration;

import cn.springcloud.gray.server.plugin.discovery.nacos.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(NacosServiceDiscovery.class)
public class GrayServerPluginNacosDiscoveryAutoConfiguration {

    @Bean
    public NacosServiceDiscovery nacosServiceDiscovery(NacosDiscoveryProperties discoveryProperties){
        return new NacosServiceDiscovery(discoveryProperties);
    }
}
