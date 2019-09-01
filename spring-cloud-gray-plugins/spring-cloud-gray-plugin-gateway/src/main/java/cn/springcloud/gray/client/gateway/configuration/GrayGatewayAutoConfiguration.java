package cn.springcloud.gray.client.gateway.configuration;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.gateway.GatewayRequestInterceptor;
import cn.springcloud.gray.client.gateway.GrayGlobalFilter;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "gray.enabled")
public class GrayGatewayAutoConfiguration {

    @Autowired
    private GrayRequestProperties grayRequestProperties;

    @Bean
    public GrayGlobalFilter grayGlobalFilter(RoutingConnectionPoint routingConnectionPoint) {
        return new GrayGlobalFilter(grayRequestProperties, routingConnectionPoint);
    }


    @Configuration
    @ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
    public static class GrayTrackZuulConfiguration {


        @Bean
        public GatewayRequestInterceptor gatewayRequestInterceptor() {
            return new GatewayRequestInterceptor();
        }

    }

}
