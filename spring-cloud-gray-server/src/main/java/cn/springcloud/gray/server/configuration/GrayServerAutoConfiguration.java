package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.*;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.evictor.NoActionGrayServerEvictor;
import cn.springcloud.gray.server.manager.DefaultGrayServiceManager;
import cn.springcloud.gray.server.manager.GrayServiceManager;
import cn.springcloud.gray.server.module.GrayModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({GrayServerProperties.class})
@Import(value = {WebConfiguration.class})
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class GrayServerAutoConfiguration {

    @Autowired
    private GrayServerProperties grayServerConfig;

    @Autowired
    private GrayModule grayModule;


    @Bean
    @ConditionalOnMissingBean
    public DefaultGrayServiceManager defaultGrayServiceManager(GrayServerEvictor grayServerEvictor) {
        return new DefaultGrayServiceManager(grayServerConfig, grayModule, grayServerEvictor);
    }


    @Bean
    public GrayServerInitializingDestroyBean grayServerInitializingBean(GrayServiceManager grayServiceManager) {
        return new GrayServerInitializingDestroyBean(grayServiceManager);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayServerEvictor grayServerEvictor() {
        return NoActionGrayServerEvictor.INSTANCE;
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public GrayServerEvictor grayServerEvictor(@Autowired(required = false) EurekaClient eurekaClient) {
//        return eurekaClient == null ? NoActionGrayServerEvictor.INSTANCE : new EurekaGrayServerEvictor(eurekaClient);
//    }
}
