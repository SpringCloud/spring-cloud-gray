package cn.springcloud.gray.server;

import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.client.ClientRemoteModule;
import cn.springcloud.gray.server.module.client.DefaultClientRemoteModule;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2019-11-25 22:45
 */
@Configuration
public class ClientRemoteConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ClientRemoteModule clientRemoteModule(ServiceDiscovery serviceDiscovery, GrayServerModule grayServerModule) {
        return new DefaultClientRemoteModule(serviceDiscovery, grayServerModule);
    }

}
