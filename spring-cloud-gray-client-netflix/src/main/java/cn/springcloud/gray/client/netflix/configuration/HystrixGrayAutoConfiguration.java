package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.client.netflix.configuration.properties.GrayHystrixProperties;
import cn.springcloud.gray.client.netflix.hystrix.GrayHystrixContextConcurrencyStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrayHystrixProperties.class)
@ConditionalOnProperty(value = {"gray.enabled", "gray.hystrix.enabled"})
public class HystrixGrayAutoConfiguration {



    @Configuration
    @ConditionalOnProperty(value = {"gray.hystrix.threadTransmitStrategy"},
            havingValue = "WRAP_CALLABLE", matchIfMissing = true)
    public static class GrayHystrixContextConcurrencyStrategyConfiguration{

        @Bean
        public GrayHystrixContextConcurrencyStrategy grayHystrixContextConcurrencyStrategy(){
            return new GrayHystrixContextConcurrencyStrategy();
        }
    }




}
