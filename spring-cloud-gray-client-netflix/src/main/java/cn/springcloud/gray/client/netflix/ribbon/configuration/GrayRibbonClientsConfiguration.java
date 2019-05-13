package cn.springcloud.gray.client.netflix.ribbon.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.GrayClientHolder;
import cn.springcloud.gray.client.netflix.ribbon.GrayLoadBalanceRule;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayRibbonClientsConfiguration implements InitializingBean {

    @Autowired
    private GrayManager grayManager;
    @Autowired
    private RequestLocalStorage requestLocalStorage;
    @Autowired
    private ServerExplainer<Server> serverExplainer;

    @Bean
    public IRule ribbonRule(@Autowired(required = false) IClientConfig config) {
        GrayLoadBalanceRule rule = new GrayLoadBalanceRule(grayManager, requestLocalStorage, serverExplainer);
        rule.initWithNiwsConfig(config);
        return rule;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        GrayClientHolder.setGrayManager(grayManager);
        GrayClientHolder.setRequestLocalStorage(requestLocalStorage);
        GrayClientHolder.setServerExplainer(serverExplainer);
    }
}
