package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.refresh.*;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-05 09:12
 */
@Configuration
public class GrayClientRefreshAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(value = "gray.load.enabled", havingValue = "true")
    public class GrayLoadRefreshConfiguration {

        @Bean
        public GrayTrackConfigurationRefresher grayTrackConfigurationRefresher(
                GrayLoadProperties grayLoadProperties, GrayTrackHolder grayTrackHolder) {
            return new GrayTrackConfigurationRefresher(grayLoadProperties, grayTrackHolder);
        }

        @Bean
        public GrayServiceConfigurationRefresher grayServiceConfigurationRefresher(
                GrayLoadProperties grayLoadProperties, GrayManager grayManager) {
            return new GrayServiceConfigurationRefresher(grayLoadProperties, grayManager);
        }
    }

    @Bean
    @ConditionalOnProperty(value = "gray.load.enabled", havingValue = "false", matchIfMissing = true)
    public GrayInformationRefresher grayInformationRefresher(
            GrayManager grayManager,
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager,
            InformationClient informationClient) {
        return new GrayInformationRefresher(grayManager, grayTrackHolder, policyDecisionManager, informationClient);
    }


    @Bean
    @ConditionalOnMissingBean
    public RefreshDriver refreshDriver(List<Refresher> refreshers) {
        return new SimpleRefreshDriver(refreshers);
    }
}
