package cn.springcloud.gray.client.plugin.dynamiclogic.configuration;

import cn.springcloud.gray.GrayManager;
import com.fm.compiler.dynamicbean.DefaultDynamicBeanManager;
import com.fm.compiler.dynamicbean.DynamicBeanManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2019-12-28 11:27
 */
@Configuration
@ConditionalOnBean(GrayManager.class)
public class DynamicBeanAutoConfigration {

    @Bean
    public DynamicBeanManager dynamicBeanManager() {
        return new DefaultDynamicBeanManager();
    }
}
