package cn.springcloud.gray.client.config;


import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.web.resources.DiscoveryInstanceResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnBean(GrayManager.class)
public class GrayClientWebConfiguration extends WebMvcConfigurerAdapter {


    @Bean
    public DiscoveryInstanceResource discoveryInstanceResource() {
        return new DiscoveryInstanceResource();
    }

}
