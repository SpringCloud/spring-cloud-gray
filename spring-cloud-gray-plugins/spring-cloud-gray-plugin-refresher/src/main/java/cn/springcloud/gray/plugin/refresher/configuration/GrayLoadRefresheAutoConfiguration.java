package cn.springcloud.gray.plugin.refresher.configuration;

import cn.springcloud.gray.plugin.refresher.EnvironmentChangeListener;
import cn.springcloud.gray.refresh.RefreshDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2019-12-20 13:07
 */
@Configuration
public class GrayLoadRefresheAutoConfiguration {

    @Bean
    public EnvironmentChangeListener environmentChangeListener(RefreshDriver refreshDriver) {
        return new EnvironmentChangeListener(refreshDriver);
    }
}
