package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.client.GrayClientInitializingDestroyBean;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@EnableConfigurationProperties({GrayClientProperties.class, GrayRequestProperties.class})
@ConditionalOnBean(GrayClientMarkerConfiguration.GrayClientMarker.class)
@Import({GrayDecisionFactoryConfiguration.class, GrayTrackConfiguration.class})
public class GrayClientAutoConfiguration {


    @Autowired
    private GrayClientProperties grayClientProperties;


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "gray.client.serverUrl")
    public InformationClient informationClient() {
        InformationClient httpClient = new HttpInformationClient(grayClientProperties.getServerUrl());
        if (grayClientProperties.isRetryable()) {
            return new RetryableInformationClient(Math.max(3, grayClientProperties.getRetryNumberOfRetries()), httpClient);
        } else {
            return httpClient;
        }
    }


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
    @ConditionalOnBean({CommunicableGrayManager.class, InstanceLocalInfo.class})
    public GrayClientInitializingDestroyBean grayClientInitializingDestroyBean(
            CommunicableGrayManager grayManager, InstanceLocalInfo instanceLocalInfo) {
        return new GrayClientInitializingDestroyBean(grayManager, grayClientProperties, instanceLocalInfo);
    }


    @Bean
    @ConditionalOnMissingBean
    public RequestLocalStorage requestLocalStorage() {
        return new ThreadLocalRequestStorage();
    }


}
