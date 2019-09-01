package cn.springcloud.gray.client.netflix.ribbon.configuration;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.client.netflix.ribbon.GrayChooserRule;
import cn.springcloud.gray.client.netflix.ribbon.GrayLoadBalanceRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayRibbonClientsConfiguration {

    @Bean
    public IRule ribbonRule(
            @Autowired(required = false) IClientConfig config) {
        ZoneAvoidanceRule rule = null;
        if(GrayClientHolder.getServerChooser()!=null){
            rule = new GrayChooserRule();
        }else{
            rule = new GrayLoadBalanceRule();
        }

        rule.initWithNiwsConfig(config);
        return rule;
    }

}
