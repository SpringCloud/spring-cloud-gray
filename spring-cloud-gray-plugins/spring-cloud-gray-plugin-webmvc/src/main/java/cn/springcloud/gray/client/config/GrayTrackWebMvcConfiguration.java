package cn.springcloud.gray.client.config;

import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.client.netflix.hystrix.HystrixLocalStorageCycle;
import cn.springcloud.gray.client.netflix.hystrix.HystrixRequestLocalStorage;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.GrayTrackFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.Filter;

@Configuration
@ConditionalOnBean(GrayTrackHolder.class)
@ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
public class GrayTrackWebMvcConfiguration {

    public static final int FILTER_BASE_ORDER = FilterRegistrationBean.LOWEST_PRECEDENCE - 10000;

    @Autowired
    private GrayTrackProperties grayTrackProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Filter.class)
    public GrayTrackFilter grayTrackFilter(
            GrayTrackHolder grayTrackHolder, RequestLocalStorage requestLocalStorage) {
        return new GrayTrackFilter(grayTrackHolder, requestLocalStorage);
    }


    @Bean
    @ConditionalOnBean(GrayTrackFilter.class)
    @ConditionalOnClass(FilterRegistrationBean.class)
    public FilterRegistrationBean grayTraceFilter(GrayTrackFilter filter) {
        GrayTrackProperties.Web webProperties = grayTrackProperties.getWeb();
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(filter);
        //拦截规则
        for (String pattern : webProperties.getPathPatterns()) {
            registration.addUrlPatterns(pattern);
        }
        //过滤器名称
        registration.setName("GrayTrackFilter");
        //过滤器顺序
        registration.setOrder(FILTER_BASE_ORDER);
        return registration;
    }


    //    @Configuration
    @ConditionalOnProperty(value = {"gray.hystrix.threadTransmitStrategy"}, havingValue = "HYSTRIX_REQUEST_LOCAL_STORAGE")
    @Import(HystrixGrayTrackWebConfiguration.class)
    public static class HystrixRequestLocalStorageConfiguration {
        @Bean
        public RequestLocalStorage requestLocalStorage() {
            return new HystrixRequestLocalStorage();
        }

        @Bean
        public LocalStorageLifeCycle localStorageLifeCycle() {
            return new HystrixLocalStorageCycle();
        }
    }

}
