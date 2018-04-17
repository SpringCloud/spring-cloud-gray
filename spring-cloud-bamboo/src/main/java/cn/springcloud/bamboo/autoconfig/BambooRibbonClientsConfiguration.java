package cn.springcloud.bamboo.autoconfig;


import cn.springcloud.bamboo.ribbon.loadbalancer.BambooZoneAvoidanceRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BambooRibbonClientsConfiguration {

    @Autowired(required = false)
    private IClientConfig config;

    @Bean
    @ConditionalOnMissingBean(value = {BambooAutoConfiguration.UnUseBambooIRule.class})
    public IRule ribbonRule() {
        BambooZoneAvoidanceRule rule = new BambooZoneAvoidanceRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }
}
