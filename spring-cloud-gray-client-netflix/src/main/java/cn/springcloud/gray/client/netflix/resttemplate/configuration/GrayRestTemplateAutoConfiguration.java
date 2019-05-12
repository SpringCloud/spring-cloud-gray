package cn.springcloud.gray.client.netflix.resttemplate.configuration;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.resttemplate.GrayClientHttpRequestIntercptor;
import cn.springcloud.gray.client.netflix.resttemplate.RestTemplateRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@ConditionalOnClass(value = RestTemplate.class)
public class GrayRestTemplateAutoConfiguration {

    @Autowired
    private GrayRequestProperties grayRequestProperties;
    @Autowired
    private RibbonConnectionPoint ribbonConnectionPoint;


    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add(grayClientHttpRequestIntercptor);
        return restTemplate;
    }


    @Bean
    public GrayClientHttpRequestIntercptor grayClientHttpRequestIntercptor(@LoadBalanced List<RestTemplate> restTemplates) {
        GrayClientHttpRequestIntercptor intercptor = new GrayClientHttpRequestIntercptor(
                grayRequestProperties, ribbonConnectionPoint);
        restTemplates.forEach(restTemplate -> restTemplate.getInterceptors().add(intercptor));
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
