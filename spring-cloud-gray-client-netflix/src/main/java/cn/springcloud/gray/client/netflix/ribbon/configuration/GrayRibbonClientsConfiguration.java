package cn.springcloud.gray.client.netflix.ribbon.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.ribbon.GrayLoadBalanceRule;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayRibbonClientsConfiguration {

    @Bean
    public IRule ribbonRule(
            @Autowired(required = false) IClientConfig config,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            ServerExplainer<Server> serverExplainer) {
        GrayLoadBalanceRule rule = new GrayLoadBalanceRule(grayManager, requestLocalStorage, serverExplainer);
        rule.initWithNiwsConfig(config);
        return rule;
    }

}
