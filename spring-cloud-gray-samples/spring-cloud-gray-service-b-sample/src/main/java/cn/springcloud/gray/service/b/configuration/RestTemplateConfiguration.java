package cn.springcloud.gray.service.b.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
