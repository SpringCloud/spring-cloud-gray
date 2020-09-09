package cn.springcloud.gray.server.plugin.discovery.nacos.configuration;

import cn.springcloud.gray.server.discovery.InstanceInfoAnalyser;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.plugin.discovery.nacos.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrayServerPluginNacosDiscoveryAutoConfiguration {

    @Bean
    public ServiceDiscovery serviceDiscovery(
            NacosDiscoveryProperties discoveryProperties,
            List<InstanceInfoAnalyser<Instance>> instanceInfoAnalysers) {
        return new NacosServiceDiscovery(discoveryProperties, instanceInfoAnalysers);
    }
}
