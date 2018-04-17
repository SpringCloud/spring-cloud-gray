package cn.springcloud.gray.client.config;

import cn.springcloud.gray.ribbon.GrayLoadBalanceRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayRibbonClientsConfiguration {


    @Bean
    public IRule ribbonRule(@Autowired(required = false) IClientConfig config) {
        GrayLoadBalanceRule rule = new GrayLoadBalanceRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }
}
