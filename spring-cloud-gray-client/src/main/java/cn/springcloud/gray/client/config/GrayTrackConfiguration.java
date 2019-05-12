package cn.springcloud.gray.client.config;


import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.web.GrayTrackFilter;
import cn.springcloud.gray.web.GrayTrackRequestInterceptor;
import cn.springcloud.gray.web.tracker.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
@EnableConfigurationProperties(GrayTrackProperties.class)
public class GrayTrackConfiguration {


    @ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
    @Configuration
    public static class GrayClientWebConfiguration extends WebMvcConfigurerAdapter {

        @Autowired
        private GrayTrackProperties grayTrackProperties;

        @Autowired
        private List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors;

        @Autowired
        private RequestLocalStorage requestLocalStorage;

//        @Bean
//        @ConditionalOnMissingBean
//        public GrayTrackInterceptor grayTrackInterceptor() {
//            return new GrayTrackInterceptor(requestLocalStorage, trackors);
//        }

//        @Override
//        public void addInterceptors(InterceptorRegistry registry) {
//            InterceptorRegistration grayTrackRegistor = registry.addInterceptor(grayTrackInterceptor());
//            GrayTrackProperties.Web webProperties = grayTrackProperties.getWeb();
//
//            for (String pattern : webProperties.getPathPatterns()) {
//                grayTrackRegistor.addPathPatterns(pattern);
//            }
//            for (String pattern : webProperties.getExcludePathPatterns()) {
//                grayTrackRegistor.excludePathPatterns(pattern);
//            }
//        }

        @Bean
        @ConditionalOnMissingBean
        public GrayTrackFilter grayTrackFilter() {
            return new GrayTrackFilter(requestLocalStorage, trackors);
        }


        @Bean
        public FilterRegistrationBean companyUrlFilterRegister(GrayTrackFilter filter) {
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
            registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
            return registration;
        }


        @Bean
        public HttpReceiveGrayTracker httpReceiveGrayTracker() {
            return new HttpReceiveGrayTracker();
        }

        @Bean
        @ConditionalOnProperty(value = "gray.request.track.web.need.headers")
        public HttpHeaderGrayTracker httpHeaderGrayTracker() {
            String header = grayTrackProperties.getWeb().getNeed().get(GrayTrackProperties.Web.NEED_HEADERS);
            String[] headers = StringUtils.isEmpty(header) ? new String[]{} : header.split(",");
            return new HttpHeaderGrayTracker(headers);
        }

        @Bean
        @ConditionalOnProperty(value = "gray.request.track.web.need.method", havingValue = "enable")
        public HttpMethodGrayTracker httpMethodGrayTracker() {
            return new HttpMethodGrayTracker();
        }

        @Bean
        @ConditionalOnProperty(value = "gray.request.track.web.need.uri", havingValue = "enable")
        public HttpURIGrayTracker httpURIGrayTracker() {
            return new HttpURIGrayTracker();
        }

        @Bean
        @ConditionalOnProperty(value = "gray.request.track.web.need.ip", havingValue = "enable")
        public HttpIPGrayTracker httpIPGrayTracker() {
            return new HttpIPGrayTracker();
        }

        @Bean
        @ConditionalOnProperty(value = "gray.request.track.web.need.parameters", havingValue = "enable")
        public HttpParameterGrayTracker httpParameterGrayTracker() {
            String name = grayTrackProperties.getWeb().getNeed().get(GrayTrackProperties.Web.NEED_PARAMETERS);
            String[] names = StringUtils.isEmpty(name) ? new String[]{} : name.split(",");
            return new HttpParameterGrayTracker(names);
        }


        @Bean
        public GrayTrackRequestInterceptor grayTrackRequestInterceptor() {
            return new GrayTrackRequestInterceptor(grayTrackProperties);
        }

    }


}
