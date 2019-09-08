package cn.springcloud.gray.client.netflix.eureka.configuration;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.client.netflix.eureka.*;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import cn.springcloud.gray.servernode.ServerListProcessor;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnBean({GrayManager.class})
@ConditionalOnProperty(value = "gray.enabled")
@ConditionalOnClass({EurekaClient.class})
public class GrayClientEurekaAutoConfiguration {


    @Autowired
    private SpringClientFactory springClientFactory;

    @Bean
    public EurekaInstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer() {
        return new EurekaInstanceLocalInfoInitiralizer();
    }

//    @Bean
    public ServerExplainer<Server> serverExplainer() {
        return new EurekaServerExplainer(springClientFactory);
    }


    @Bean
    public InstanceDiscoveryClient instanceDiscoveryClient() {
        return new EurekaInstanceDiscoveryClient();
    }


    @Bean
    @ConditionalOnProperty(value = "gray.holdout-server.enabled")
    @ConditionalOnMissingBean
    public ServerListProcessor serverListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, EurekaClient eurekaClient) {
        if(grayHoldoutServerProperties.isZoneAffinity()){
            return new EurekaZoneAffinityServerListProcessor(grayHoldoutServerProperties, eurekaClient);
        }
        return new EurekaServerListProcessor(grayHoldoutServerProperties, eurekaClient);
    }


}
