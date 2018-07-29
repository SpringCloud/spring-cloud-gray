package cn.springcloud.gray.client.config;

import cn.springcloud.bamboo.BambooConstants;
import cn.springcloud.bamboo.autoconfig.BambooAutoConfiguration;
import cn.springcloud.gray.DefaultGrayManager;
import cn.springcloud.gray.HttpInformationClient;
import cn.springcloud.gray.RetryableInformationClient;
import cn.springcloud.gray.client.GrayClientInitializingBean;
import cn.springcloud.gray.client.GrayOptionalArgs;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.core.GrayDecisionFactory;
import cn.springcloud.gray.core.GrayManager;
import cn.springcloud.gray.core.InformationClient;
import cn.springcloud.gray.decision.DefaultGrayDecisionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({GrayClientProperties.class})
@ConditionalOnBean(GrayClientMarkerConfiguration.GrayClientMarker.class)
@RibbonClients(defaultConfiguration = GrayRibbonClientsConfiguration.class)
public class GrayClientAutoConfiguration {


    @Bean
    public BambooAutoConfiguration.UnUseBambooIRule unUseBambooIRule() {
        return new BambooAutoConfiguration.UnUseBambooIRule();
    }


//    @Bean
//    public IRule ribbonRule(@Autowired(required = false) IClientConfig config) {
//        GrayLoadBalanceRule rule = new GrayLoadBalanceRule();
//        rule.initWithNiwsConfig(config);
//        return rule;
//    }

    @Bean
    @Order(value = BambooConstants.INITIALIZING_ORDER + 1)
    public GrayClientInitializingBean grayClientInitializingBean() {
        return new GrayClientInitializingBean();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayDecisionFactory grayDecisionFactory() {
        return new DefaultGrayDecisionFactory();
    }


    @Configuration
    @ConditionalOnProperty(prefix = "gray.client", value = "information-client", havingValue = "http", matchIfMissing
            = true)
    public static class HttpGrayManagerClientConfiguration {
        @Autowired
        private GrayClientProperties grayClientProperties;

        @Bean
        public InformationClient informationClient() {
            InformationClient client = new HttpInformationClient(grayClientProperties.getServerUrl(), new
                    RestTemplate());
            if (!grayClientProperties.isRetryable()) {
                return client;
            }
            return new RetryableInformationClient(grayClientProperties.getRetryNumberOfRetries(), client);
        }


        @Bean
        public GrayManager grayManager(InformationClient informationClient, GrayDecisionFactory grayDecisionFactory) {
            GrayOptionalArgs args = new GrayOptionalArgs();
            args.setDecisionFactory(grayDecisionFactory);
            args.setGrayClientConfig(grayClientProperties);
            args.setInformationClient(informationClient);
            return new DefaultGrayManager(args);
        }
    }
}
