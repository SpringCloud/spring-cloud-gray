package cn.springcloud.gray.client.config;

import cn.springcloud.gray.local.DefaultInstanceLocalInfoInitiralizer;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springcloud.gray.servernode.DefaultInstanceDiscoveryClient;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "gray.enabled")
public class GrayClientDiscoveryAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public InstanceDiscoveryClient instanceDiscoveryClient(){
        return new DefaultInstanceDiscoveryClient();
    }


    @Bean
    @ConditionalOnMissingBean
    public InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer(){
        return new DefaultInstanceLocalInfoInitiralizer();
    }

}
