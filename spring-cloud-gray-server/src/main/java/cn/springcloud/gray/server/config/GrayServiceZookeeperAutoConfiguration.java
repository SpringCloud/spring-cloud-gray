package cn.springcloud.gray.server.config;

import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.GrayServerEvictor;
import cn.springcloud.gray.server.ZookeeperGrayServerEvictor;
import cn.springcloud.gray.server.service.AbstractGrayService;
import cn.springcloud.gray.server.service.ZookeeperGrayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: duozl
 * @Date: 2018/6/4 18:59
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration")
public class GrayServiceZookeeperAutoConfiguration {

    private final DiscoveryClient discoveryClient;
    private final GrayServiceManager grayServiceManager;

    @Autowired
    public GrayServiceZookeeperAutoConfiguration(DiscoveryClient discoveryClient,
                                                 GrayServiceManager grayServiceManager) {
        this.discoveryClient = discoveryClient;
        this.grayServiceManager = grayServiceManager;
    }

    @Bean
    public AbstractGrayService grayService() {
        return new ZookeeperGrayService(discoveryClient, grayServiceManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayServerEvictor grayServerEvictor() {
        return new ZookeeperGrayServerEvictor(discoveryClient);
    }
}
