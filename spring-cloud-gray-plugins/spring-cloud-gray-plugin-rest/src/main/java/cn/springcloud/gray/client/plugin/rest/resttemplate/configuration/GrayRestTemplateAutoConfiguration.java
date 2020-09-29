package cn.springcloud.gray.client.plugin.rest.resttemplate.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.plugin.rest.resttemplate.GrayClientHttpRequestIntercptor;
import cn.springcloud.gray.client.plugin.rest.resttemplate.RestTemplateRequestInterceptor;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@ConditionalOnBean(GrayManager.class)
@ConditionalOnClass(value = {RestTemplate.class, LoadBalanced.class})
public class GrayRestTemplateAutoConfiguration {


    @Configuration
    @ConditionalOnBean(RestTemplate.class)
    public static class LoadBalanceRestTemplateConfiguration {
        @Autowired
        private GrayRequestProperties grayRequestProperties;
        @Autowired
        private RoutingConnectionPoint routingConnectionPoint;


        @Bean
        public GrayClientHttpRequestIntercptor grayClientHttpRequestIntercptor(
                @Autowired(required = false) @LoadBalanced List<RestTemplate> restTemplates) {
            GrayClientHttpRequestIntercptor intercptor = new GrayClientHttpRequestIntercptor(
                    grayRequestProperties, routingConnectionPoint);
            if (restTemplates != null) {
                restTemplates.forEach(restTemplate -> restTemplate.getInterceptors().add(intercptor));
            }
            return intercptor;
        }


        @Configuration
        @ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
        public static class GrayTrackRestTemplateConfiguration {

            @Bean
            public RestTemplateRequestInterceptor restTemplateRequestInterceptor() {
                return new RestTemplateRequestInterceptor();
            }

        }
    }


}
