package cn.springcloud.gray.client.config;

import cn.springcloud.gray.handle.DefaultHandleManager;
import cn.springcloud.gray.handle.DefaultHandleRuleManager;
import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.handle.HandleRuleManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-05-27 10:55
 */
@Configuration
@ConditionalOnProperty(value = "gray.enabled")
public class GrayClientHandleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HandleManager handleManager() {
        return new DefaultHandleManager();
    }


    @Bean
    @ConditionalOnMissingBean
    public HandleRuleManager handleRuleManager() {
        return new DefaultHandleRuleManager();
    }


}
