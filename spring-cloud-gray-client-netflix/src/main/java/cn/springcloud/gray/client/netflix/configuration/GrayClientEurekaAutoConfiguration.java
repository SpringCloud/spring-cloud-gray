package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.client.netflix.eureka.EurekaInstanceDiscoveryClient;
import cn.springcloud.gray.client.netflix.eureka.EurekaInstanceLocalInfoInitiralizer;
import cn.springcloud.gray.client.netflix.eureka.EurekaServerExplainer;
import cn.springcloud.gray.client.netflix.eureka.EurekaServerListProcessor;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import cn.springcloud.gray.servernode.ServerListProcessor;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnBean({GrayManager.class})
@ConditionalOnProperty(value = "gray.enabled")
@ConditionalOnBean({EurekaClient.class})
public class GrayClientEurekaAutoConfiguration {


    @Autowired
    private SpringClientFactory springClientFactory;

    @Bean
    @ConditionalOnMissingBean
    public EurekaInstanceLocalInfoInitiralizer eurekaInstanceLocalInfoInitiralizer() {
        return new EurekaInstanceLocalInfoInitiralizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public EurekaServerExplainer eurekaServerExplainer() {
        return new EurekaServerExplainer(springClientFactory);
    }


    @Bean
    public InstanceDiscoveryClient instanceDiscoveryClient() {
        return new EurekaInstanceDiscoveryClient();
    }


    @Bean
    @ConditionalOnProperty(value = "gray.holdoutServer.enabled")
    @ConditionalOnMissingBean
    public ServerListProcessor serverListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, EurekaClient eurekaClient) {
        return new EurekaServerListProcessor(grayHoldoutServerProperties, eurekaClient);
    }


}
