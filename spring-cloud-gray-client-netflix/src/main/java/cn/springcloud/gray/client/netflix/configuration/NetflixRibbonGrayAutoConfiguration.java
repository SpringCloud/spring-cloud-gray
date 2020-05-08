package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.ribbon.RibbonServerExplainer;
import cn.springcloud.gray.client.netflix.ribbon.configuration.GrayRibbonClientsConfiguration;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.routing.connectionpoint.DefaultRoutingConnectionPoint;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.servernode.ServerExplainer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(GrayManager.class)
@RibbonClients(defaultConfiguration = GrayRibbonClientsConfiguration.class)
public class NetflixRibbonGrayAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public RoutingConnectionPoint ribbonConnectionPoint(
            GrayManager grayManager, RequestLocalStorage requestLocalStorage, LocalStorageLifeCycle localStorageLifeCycle) {
        return new DefaultRoutingConnectionPoint(grayManager, requestLocalStorage, localStorageLifeCycle);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerExplainer<Server> ribbonServerExplainer(SpringClientFactory springClientFactory) {
        return new RibbonServerExplainer(springClientFactory);
    }

}
