package cn.springcloud.gray.client.config;

import cn.springcloud.gray.client.config.properties.GrayServerProperties;
import cn.springcloud.gray.communication.HttpInformationClient;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.communication.RetryableInformationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnProperty(value = "gray.server.url")
public class InformationClientConfiguration {


    @Configuration
    @ConditionalOnProperty(value = "gray.server.loadbalanced", havingValue = "false", matchIfMissing = true)
    public static class DefaultGrayInformationRestTemplate {
        @Bean("grayInformationRestTemplate")
        @ConditionalOnMissingBean(name = {"grayInformationRestTemplate"})
        public RestTemplate grayInformationRestTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "gray.server.loadbalanced")
    public static class LoadBalancedGrayInformationRestTemplate {
        @Bean("grayInformationRestTemplate")
        @LoadBalanced
        @ConditionalOnMissingBean(name = {"grayInformationRestTemplate"})
        public RestTemplate grayInformationRestTemplate() {
            return new RestTemplate();
        }
    }


    @Bean
    @ConditionalOnMissingBean
    public InformationClient informationClient(
            @Autowired(required = false) RestTemplate grayInformationRestTemplate,
            GrayServerProperties grayServerProperties) {
        InformationClient httpClient = new HttpInformationClient(grayServerProperties.getUrl(), grayInformationRestTemplate);
        if (grayServerProperties.isRetryable()) {
            return new RetryableInformationClient(Math.max(3, grayServerProperties.getRetryNumberOfRetries()), httpClient);
        } else {
            return httpClient;
        }
    }
}
