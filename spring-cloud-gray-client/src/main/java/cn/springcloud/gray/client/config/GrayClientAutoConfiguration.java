package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.client.GrayClientInitializingDestroyBean;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({GrayClientProperties.class})
@ConditionalOnBean(GrayClientMarkerConfiguration.GrayClientMarker.class)
public class GrayClientAutoConfiguration {


    @Autowired
    private GrayClientProperties grayClientProperties;


    @Bean
    @ConditionalOnMissingBean
    public GrayManager grayManager(List<GrayDecisionFactory> decisionFactories,
                                   List<RequestInterceptor> requestInterceptors) {
        return new DefaultGrayManager(grayClientProperties, decisionFactories, requestInterceptors);
    }

    @Bean
    @ConditionalOnBean({CommunicableGrayManager.class, InstanceLocalInfo.class})
    public GrayClientInitializingDestroyBean grayClientInitializingDestroyBean(
            CommunicableGrayManager grayManager, InstanceLocalInfo instanceLocalInfo) {
        return new GrayClientInitializingDestroyBean(grayManager, instanceLocalInfo);
    }


}
