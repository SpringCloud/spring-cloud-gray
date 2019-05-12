package cn.springcloud.gray.client.netflix.feign.configuration;

import cn.springcloud.gray.client.netflix.configuration.HystrixGrayAutoConfiguration;
import cn.springcloud.gray.client.netflix.feign.GrayTrackFeignRequestInterceptor;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.loadbalancer.ILoadBalancer;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by saleson on 2017/11/9.
 */
@ConditionalOnClass(value = {ILoadBalancer.class, Feign.class})
@Configuration
@EnableFeignClients(defaultConfiguration = {GrayFeignClientsConfiguration.class})
public class GrayFeignAutoConfiguration {


    @Configuration
    @ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
    public static class GrayTrackFeignConfiguration {


//        @Bean
//        public FeignRequestInterceptor feignRequestInterceptor() {
//            return new FeignRequestInterceptor();
//        }

        @Bean
        public GrayTrackFeignRequestInterceptor grayTrackFeignRequestInterceptor(RequestLocalStorage requestLocalStorage) {
            return new GrayTrackFeignRequestInterceptor(requestLocalStorage);
        }

    }


    @Configuration
    @ConditionalOnProperty(value = "feign.hystrix.enabled")
    public static class HystrixConfiguration extends HystrixGrayAutoConfiguration {

    }

}
