package cn.springcloud.gray.server.event.longpolling.configuration;

import cn.springcloud.gray.server.event.longpolling.DefaultLongPollingManager;
import cn.springcloud.gray.server.event.longpolling.LongPollingGrayEventSender;
import cn.springcloud.gray.server.event.longpolling.LongPollingManager;
import cn.springcloud.gray.server.event.longpolling.configuration.properties.EventLongPollingProperties;
import cn.springcloud.gray.event.server.GrayEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-02-03 21:08
 */
@Configuration
@ComponentScan({"cn.springcloud.gray.server.event.longpolling.resources"})
@EnableConfigurationProperties(EventLongPollingProperties.class)
public class LongPollingEventAutoConfiguration {

    @Autowired
    private EventLongPollingProperties eventLongPollingProperties;

    @Bean
    @ConditionalOnMissingBean
    public LongPollingManager longPollingManager(EventLongPollingProperties eventLongPollingProperties) {
        return new DefaultLongPollingManager(eventLongPollingProperties);
    }


    @Bean
    public GrayEventSender grayEventSender(LongPollingManager longPollingManager) {
        return new LongPollingGrayEventSender(longPollingManager);
    }

}
