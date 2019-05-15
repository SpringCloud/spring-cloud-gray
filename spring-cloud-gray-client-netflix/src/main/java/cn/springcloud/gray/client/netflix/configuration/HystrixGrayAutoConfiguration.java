package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.hystrix.HystrixRequestLocalStorage;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.web.GrayTrackFilter;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Configuration
@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
public class HystrixGrayAutoConfiguration {


    @Autowired
    private GrayManager grayManager;


    @Bean
    public RequestLocalStorage requestLocalStorage() {
        return new HystrixRequestLocalStorage();
    }


    @Bean
    public GrayTrackFilter grayTrackFilter(
            RequestLocalStorage requestLocalStorage,
            List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors) {
        return new GrayTrackFilter(requestLocalStorage, trackors) {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (!HystrixRequestContext.isCurrentThreadInitialized()) {
                    HystrixRequestContext.initializeContext();
                }
                try {
                    super.doFilter(request, response, chain);
                } finally {
                    if (HystrixRequestContext.isCurrentThreadInitialized()) {
                        HystrixRequestContext.getContextForCurrentThread().shutdown();
                    }
                }
            }
        };
    }

}
