package cn.springcloud.gray.server.config;

/**
 * @Author: duozl
 * @Date: 2018/6/4 18:59
 */

import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.EurekaGrayServerEvictor;
import cn.springcloud.gray.server.GrayServerEvictor;
import cn.springcloud.gray.server.service.AbstractGrayService;
import cn.springcloud.gray.server.service.EurekaGrayService;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "com.netflix.discovery.EurekaClient")
public class GrayServiceEurekaAutoConfiguration {

    private final EurekaClient eurekaClient;
    private final DiscoveryClient discoveryClient;
    private final GrayServiceManager grayServiceManager;

    @Autowired
    public GrayServiceEurekaAutoConfiguration(EurekaClient eurekaClient, DiscoveryClient discoveryClient,
                                              GrayServiceManager grayServiceManager) {
        this.eurekaClient = eurekaClient;
        this.discoveryClient = discoveryClient;
        this.grayServiceManager = grayServiceManager;
    }

    @Bean
    public AbstractGrayService grayService() {
        return new EurekaGrayService(eurekaClient, discoveryClient, grayServiceManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayServerEvictor grayServerEvictor() {
        return new EurekaGrayServerEvictor(eurekaClient);
    }
}
