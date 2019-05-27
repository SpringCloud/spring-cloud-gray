package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.connectionpoint.DefaultRibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.ribbon.configuration.GrayRibbonClientsConfiguration;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(GrayManager.class)
@RibbonClients(defaultConfiguration = GrayRibbonClientsConfiguration.class)
public class NetflixGrayAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public RibbonConnectionPoint ribbonConnectionPoint(
            GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
        return new DefaultRibbonConnectionPoint(grayManager, requestLocalStorage);
    }

}
