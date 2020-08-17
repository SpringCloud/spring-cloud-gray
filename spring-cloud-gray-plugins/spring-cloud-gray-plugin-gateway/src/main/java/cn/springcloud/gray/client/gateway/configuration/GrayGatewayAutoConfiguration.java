package cn.springcloud.gray.client.gateway.configuration;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.gateway.GatewayRequestInterceptor;
import cn.springcloud.gray.client.gateway.GrayLoadBalancerClientFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "gray.enabled")
public class GrayGatewayAutoConfiguration {

    @Autowired
    private GrayRequestProperties grayRequestProperties;

    //    @Bean
//    public GrayGlobalFilter grayGlobalFilter(RoutingConnectionPoint routingConnectionPoint, RequestLocalStorage requestLocalStorage) {
//        return new GrayGlobalFilter(grayRequestProperties, routingConnectionPoint, requestLocalStorage);
//    }

    @Bean
    @ConditionalOnBean(LoadBalancerClient.class)
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client, LoadBalancerProperties properties) {
        return new GrayLoadBalancerClientFilter(client, properties);
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
