package cn.springcloud.gray.client.netflix.zuul.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.netflix.configuration.HystrixGrayAutoConfiguration;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.zuul.GrayPostZuulFilter;
import cn.springcloud.gray.client.netflix.zuul.GrayPreZuulFilter;
import cn.springcloud.gray.client.netflix.zuul.ZuulRequestInterceptor;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(
        value = {GrayManager.class},
        name = {"zuulServlet"})
@ConditionalOnClass(value = ZuulServlet.class)
public class GrayZuulAutoConfiguration {

    @Autowired
    private GrayRequestProperties grayRequestProperties;

    @Bean
    public GrayPreZuulFilter grayPreZuulFilter(RibbonConnectionPoint ribbonConnectionPoint) {
        return new GrayPreZuulFilter(grayRequestProperties, ribbonConnectionPoint);
    }

    @Bean
    public GrayPostZuulFilter grayPostZuulFilter(RibbonConnectionPoint ribbonConnectionPoint) {
        return new GrayPostZuulFilter(ribbonConnectionPoint);
    }


    @Configuration
    @ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
    public static class GrayTrackZuulConfiguration {


        @Bean
        public ZuulRequestInterceptor zuulRequestInterceptor() {
            return new ZuulRequestInterceptor();
        }

    }


    @Configuration
    @ConditionalOnProperty(value = "zuul.ribbonIsolationStrategy", havingValue = "THREAD")
    public static class HystrixConfiguration extends HystrixGrayAutoConfiguration {

    }

}
