package cn.springcloud.gray.client.config;


import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.track.DefaultGrayTrackHolder;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.GrayTrackRequestInterceptor;
import cn.springcloud.gray.web.tracker.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
@EnableConfigurationProperties(GrayTrackProperties.class)
public class GrayTrackConfiguration {


    @Bean()
    @ConditionalOnMissingBean
    public GrayTrackHolder grayTrackHolder(
            List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers) {
        return new DefaultGrayTrackHolder(trackers, null);
    }


    @Configuration
    @ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
    public static class GrayHttpTrackerConfiguration {

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
