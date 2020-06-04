package cn.springcloud.gray.client.config;

import cn.springcloud.gray.changed.notify.ChangedNotifyDriver;
import cn.springcloud.gray.changed.notify.DefaultChangedNotifyDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-06-01 11:14
 */
@Configuration
@ConditionalOnProperty(value = "gray.enabled")
public class ChangedNotifyAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ChangedNotifyDriver changedNotifyDriver() {
        return new DefaultChangedNotifyDriver();
    }


}
