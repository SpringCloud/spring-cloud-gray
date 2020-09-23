package cn.springcloud.gray.client.config;

import cn.springcloud.gray.hook.HookRegistory;
import cn.springcloud.gray.hook.SimpleHookRegistory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-09-23 22:50
 */
@Configuration
public class GrayCompendontConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HookRegistory hookDriver() {
        return new SimpleHookRegistory();
    }
}
