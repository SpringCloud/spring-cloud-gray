package cn.springcloud.gray.client.config;


import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.DefaultGrayTrackHolder;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.GrayTrackFilter;
import cn.springcloud.gray.web.GrayTrackRequestInterceptor;
import cn.springcloud.gray.web.tracker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
@EnableConfigurationProperties(GrayTrackProperties.class)
public class GrayTrackConfiguration {


    @Bean(initMethod = "openForWork")
    @ConditionalOnMissingBean
    public GrayTrackHolder grayTrackHolder(
            GrayTrackProperties grayTrackProperties,
            @Autowired(required = false) InformationClient informationClient,
            List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers) {
        return new DefaultGrayTrackHolder(grayTrackProperties, informationClient, trackers);
    }


    @ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
    @Configuration
    public static class GrayClientWebConfiguration extends WebMvcConfigurerAdapter {

        @Autowired
        private GrayTrackProperties grayTrackProperties;

        @Autowired
        private List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors;

        @Autowired
        private RequestLocalStorage requestLocalStorage;

        @Bean
        @ConditionalOnMissingBean
        public GrayTrackFilter grayTrackFilter(GrayTrackHolder grayTrackHolder) {
            return new GrayTrackFilter(grayTrackHolder, requestLocalStorage);
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
        public HttpReceiveGrayInfoTracker httpReceiveGrayTracker() {
            return new HttpReceiveGrayInfoTracker();
        }

        @Bean
        public HttpHeaderGrayInfoTracker httpHeaderGrayTracker() {
            return new HttpHeaderGrayInfoTracker();
        }

        @Bean
        public HttpMethodGrayInfoTracker httpMethodGrayTracker() {
            return new HttpMethodGrayInfoTracker();
        }

        @Bean
        public HttpURIGrayInfoTracker httpURIGrayTracker() {
            return new HttpURIGrayInfoTracker();
        }

        @Bean
        public HttpIPGrayInfoTracker httpIPGrayTracker() {
            return new HttpIPGrayInfoTracker();
        }

        @Bean
        public HttpParameterGrayInfoTracker httpParameterGrayTracker() {
            return new HttpParameterGrayInfoTracker();
        }


        @Bean
        public GrayTrackRequestInterceptor grayTrackRequestInterceptor() {
            return new GrayTrackRequestInterceptor();
        }

    }


}
