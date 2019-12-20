package cn.springcloud.gray.plugin.refresher.configuration;

import cn.springcloud.gray.plugin.refresher.EnvironmentChangeListener;
import cn.springcloud.gray.refresh.RefreshDriver;
import cn.springcloud.gray.refresh.Refresher;
import cn.springcloud.gray.refresh.SimpleRefreshDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author saleson
 * @date 2019-12-20 13:07
 */
@Configuration
@ConditionalOnProperty(value = "gray.load.refresh.enabled", matchIfMissing = true)
public class GrayLoadRefresheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RefreshDriver refreshDriver(@Autowired List<Refresher> refreshers) {
        return new SimpleRefreshDriver(refreshers);
    }

    @Bean
    public EnvironmentChangeListener environmentChangeListener(RefreshDriver refreshDriver) {
        return new EnvironmentChangeListener(refreshDriver);
    }
}
