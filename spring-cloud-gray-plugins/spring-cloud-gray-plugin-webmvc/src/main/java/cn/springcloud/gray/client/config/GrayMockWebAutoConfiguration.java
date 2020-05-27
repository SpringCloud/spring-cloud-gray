package cn.springcloud.gray.client.config;

import cn.springcloud.gray.mock.MockManager;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.web.GrayMockFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author saleson
 * @date 2020-05-27 11:51
 */
@Configuration
@ConditionalOnBean(MockManager.class)
@ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
public class GrayMockWebAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Filter.class)
    public GrayMockFilter grayMockFilter(
            MockManager mockManager, RequestLocalStorage requestLocalStorage) {
        return new GrayMockFilter(mockManager, requestLocalStorage);
    }

    @Bean
    @ConditionalOnBean(GrayMockFilter.class)
    @ConditionalOnClass(FilterRegistrationBean.class)
    public FilterRegistrationBean mockResponseFilter(GrayMockFilter grayMockFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(grayMockFilter);
        //过滤器名称
        registration.setName("grayMockFilter");
        //过滤器顺序
        registration.setOrder(GrayTrackWebMvcConfiguration.FILTER_BASE_ORDER + 1000);
        return registration;
    }

}
