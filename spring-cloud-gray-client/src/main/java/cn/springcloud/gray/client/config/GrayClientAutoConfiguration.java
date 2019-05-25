package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.client.GrayClientEnrollInitializingDestroyBean;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.communication.HttpInformationClient;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.communication.RetryableInformationClient;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.ThreadLocalRequestStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties({GrayClientProperties.class, GrayRequestProperties.class, GrayLoadProperties.class})
//@ConditionalOnBean(GrayClientMarkerConfiguration.GrayClientMarker.class)
@ConditionalOnProperty(value = "gray.enabled")
@Import({GrayDecisionFactoryConfiguration.class, GrayTrackConfiguration.class})
public class GrayClientAutoConfiguration {


    @Autowired
    private GrayClientProperties grayClientProperties;


    @Bean
    @ConditionalOnMissingBean
    public GrayManager grayManager(
            @Autowired(required = false) GrayLoadProperties grayLoadProperties,
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            @Autowired(required = false) List<RequestInterceptor> requestInterceptors,
            InformationClient informationClient) {
        return new DefaultGrayManager(
                grayClientProperties, grayLoadProperties, grayDecisionFactoryKeeper,
                requestInterceptors, informationClient);
    }


    @Bean
    @ConditionalOnProperty(value = "gray.client.instance.grayEnroll")
    public GrayClientEnrollInitializingDestroyBean grayClientEnrollInitializingDestroyBean(
            CommunicableGrayManager grayManager, InstanceLocalInfo instanceLocalInfo) {
        return new GrayClientEnrollInitializingDestroyBean(grayManager, grayClientProperties, instanceLocalInfo);
    }


    @Bean
    @ConditionalOnMissingBean
    public RequestLocalStorage requestLocalStorage() {
        return new ThreadLocalRequestStorage();
    }


    @Configuration
    public static class InformationClientConfiguration {


        @Bean("grayInformationRestTemplate")
        @ConditionalOnMissingBean(name = {"grayInformationRestTemplate"})
        public RestTemplate grayInformationRestTemplate() {
            return new RestTemplate();
        }


        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "gray.client.serverUrl")
        public InformationClient informationClient(
                @Autowired(required = false) RestTemplate grayInformationRestTemplate,
                GrayClientProperties grayClientProperties) {
            InformationClient httpClient = new HttpInformationClient(grayClientProperties.getServerUrl(), grayInformationRestTemplate);
            if (grayClientProperties.isRetryable()) {
                return new RetryableInformationClient(Math.max(3, grayClientProperties.getRetryNumberOfRetries()), httpClient);
            } else {
                return httpClient;
            }
        }
    }

}
