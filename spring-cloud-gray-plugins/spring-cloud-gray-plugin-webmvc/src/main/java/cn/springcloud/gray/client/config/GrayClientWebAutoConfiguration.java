package cn.springcloud.gray.client.config;


import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.web.resources.DiscoveryInstanceResource;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(GrayClientAutoConfiguration.class)
@ConditionalOnBean(GrayManager.class)
//@ComponentScan(basePackages = {"cn.springcloud.gray.web.resources"})
public class GrayClientWebAutoConfiguration {

    @Bean
    public DiscoveryInstanceResource discoveryInstanceResource() {
        return new DiscoveryInstanceResource();
    }

}
