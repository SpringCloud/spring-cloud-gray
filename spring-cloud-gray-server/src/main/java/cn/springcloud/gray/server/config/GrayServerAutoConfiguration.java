package cn.springcloud.gray.server.config;

import cn.springcloud.bamboo.BambooConstants;
import cn.springcloud.gray.server.DefaultGrayServiceManager;
import cn.springcloud.gray.server.GrayServerConfig;
import cn.springcloud.gray.server.GrayServerInitializingBean;
import cn.springcloud.gray.server.config.properties.GrayServerConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Configuration
@EnableConfigurationProperties({GrayServerConfigBean.class})
@Import(value = {WebConfiguration.class})
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class GrayServerAutoConfiguration {

    @Autowired
    private GrayServerConfig grayServerConfig;

    @Bean
    @ConditionalOnMissingBean
    public DefaultGrayServiceManager defaultGrayServiceManager() {
        return new DefaultGrayServiceManager(grayServerConfig);
    }


    @Bean
    @Order(value = BambooConstants.INITIALIZING_ORDER + 1)
    public GrayServerInitializingBean grayServerInitializingBean() {
        return new GrayServerInitializingBean();
    }


//    @Bean
//    @ConditionalOnMissingBean
//    public GrayServerEvictor grayServerEvictor(@Autowired(required = false) EurekaClient eurekaClient) {
//        return eurekaClient == null ? NoActionGrayServerEvictor.INSTANCE : new EurekaGrayServerEvictor(eurekaClient);
//    }
}
