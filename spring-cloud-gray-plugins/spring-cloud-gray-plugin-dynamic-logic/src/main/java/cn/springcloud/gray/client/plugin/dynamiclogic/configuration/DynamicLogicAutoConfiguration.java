package cn.springcloud.gray.client.plugin.dynamiclogic.configuration;

import cn.springcloud.gray.client.plugin.dynamiclogic.FmDynamicLogicDriver;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDriver;
import com.fm.compiler.dynamicbean.DynamicBeanManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2019-12-28 11:27
 */
@Configuration
public class DynamicLogicAutoConfiguration {

    @Bean
    public DynamicLogicDriver dynamicLogicDriver(DynamicBeanManager dynamicBeanManager) {
        return new FmDynamicLogicDriver(dynamicBeanManager);
    }

}
