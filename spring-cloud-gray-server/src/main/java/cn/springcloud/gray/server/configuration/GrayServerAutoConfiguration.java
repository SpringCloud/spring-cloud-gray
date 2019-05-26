package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.server.GrayServerInitializingDestroyBean;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.event.DefaultGrayEventPublisher;
import cn.springcloud.gray.server.evictor.DefaultGrayServiceEvictor;
import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.evictor.NoActionGrayServerEvictor;
import cn.springcloud.gray.server.manager.DefaultGrayServiceManager;
import cn.springcloud.gray.server.manager.GrayServiceManager;
import cn.springcloud.gray.server.module.GrayModule;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.GrayServerTrackModule;
import cn.springcloud.gray.server.module.SimpleGrayModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({GrayServerProperties.class})
@Import(value = {WebConfiguration.class})
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class GrayServerAutoConfiguration {

    @Autowired
    private GrayServerProperties grayServerConfig;


    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultGrayServiceManager defaultGrayServiceManager(
            GrayServerEvictor grayServerEvictor, GrayServerModule grayServerModule) {
        return new DefaultGrayServiceManager(grayServerConfig, grayServerModule, grayServerEvictor);
    }


    @Bean
    public GrayServerInitializingDestroyBean grayServerInitializingBean(GrayServiceManager grayServiceManager) {
        return new GrayServerInitializingDestroyBean(grayServiceManager, grayServerConfig);
    }


    @Configuration
    public static class DefaultConfiguration {
        @Autowired
        private GrayServerProperties grayServerProperties;

        @Bean
        @ConditionalOnMissingBean
        public GrayServerEvictor grayServerEvictor(
                @Autowired(required = false)
                        ServiceDiscovery serviceDiscovery) {
            if (serviceDiscovery == null) {
                return NoActionGrayServerEvictor.INSTANCE;
            }
            return new DefaultGrayServiceEvictor(grayServerProperties, serviceDiscovery);

        }


        @Bean
        @ConditionalOnMissingBean
        public GrayModule grayModule(
                GrayServerModule grayServerModule, GrayServerTrackModule grayServerTrackModule,
                @Autowired(required = false) ObjectMapper objectMapper) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            return new SimpleGrayModule(grayServerProperties, grayServerModule, grayServerTrackModule, objectMapper);
        }

        @Bean
        @ConditionalOnMissingBean
        public GrayEventPublisher grayEventPublisher() {
            return new DefaultGrayEventPublisher();
        }
    }

}
