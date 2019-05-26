package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.connectionpoint.DefaultHystrixRibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.hystrix.HystrixRequestLocalStorage;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.GrayTrackFilter;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Configuration
@ConditionalOnBean(GrayManager.class)
@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
//@Import(HystrixGrayTrackWebConfiguration.class)
public class HystrixGrayAutoConfiguration {


    @Bean
    public RequestLocalStorage requestLocalStorage() {
        return new HystrixRequestLocalStorage();
    }


    /**
     * 支持hystrix使用线程隔离时依然能够进行跑线程传递GrayRequest
     *
     * @return DefaultHystrixRibbonConnectionPoint
     */
    @Bean
    public RibbonConnectionPoint hystrixRibbonConnectionPoint(
            GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
        return new DefaultHystrixRibbonConnectionPoint(grayManager, requestLocalStorage);
    }


    @Bean
    public GrayTrackFilter grayTrackFilter(
            GrayTrackHolder grayTrackHolder,
            RequestLocalStorage requestLocalStorage) {
        return new GrayTrackFilter(grayTrackHolder, requestLocalStorage) {
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
