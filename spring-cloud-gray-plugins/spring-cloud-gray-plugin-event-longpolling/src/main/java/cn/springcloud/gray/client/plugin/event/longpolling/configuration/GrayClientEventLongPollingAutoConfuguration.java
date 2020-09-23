package cn.springcloud.gray.client.plugin.event.longpolling.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.plugin.event.longpolling.GrayEventLongPollingReceiver;
import cn.springcloud.gray.client.plugin.event.longpolling.GrayEventRemoteClient;
import cn.springcloud.gray.client.plugin.event.longpolling.GrayRefreshedSortMarkListener;
import cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties.LongPollingProperties;
import cn.springcloud.gray.communication.http.HttpAgent;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.event.client.GrayEventPublisher;
import cn.springcloud.gray.event.client.GrayEventReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-02-04 19:51
 */
@Configuration
@ConditionalOnBean(GrayManager.class)
@EnableConfigurationProperties(LongPollingProperties.class)
public class GrayClientEventLongPollingAutoConfuguration {

    @Autowired
    private LongPollingProperties longPollingProperties;


    @Bean
    public GrayEventRemoteClient grayEventRemoteClient(HttpAgent httpAgent) {
        return new GrayEventRemoteClient(httpAgent);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayEventReceiver grayEventReceiver(
            GrayEventPublisher grayEventPublisher,
            GrayEventRemoteClient grayEventRemoteClient,
            InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        return new GrayEventLongPollingReceiver(
                longPollingProperties, grayEventPublisher, grayEventRemoteClient, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayRefreshedSortMarkListener grayRefreshedSortMarkListener(GrayEventReceiver grayEventReceiver) {
        return new GrayRefreshedSortMarkListener(grayEventReceiver);
    }
}
