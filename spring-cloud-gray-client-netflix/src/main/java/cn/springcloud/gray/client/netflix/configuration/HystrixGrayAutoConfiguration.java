package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.hystrix.HystrixLocalStorageCycle;
import cn.springcloud.gray.client.netflix.hystrix.HystrixRequestLocalStorage;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.hystrix.HystrixCommand;
import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnBean(GrayManager.class)
@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
@ConditionalOnProperty(value = "gray.hystrix.enabled")
@Import(HystrixGrayTrackWebConfiguration.class)
public class HystrixGrayAutoConfiguration {


    @Bean
    public RequestLocalStorage requestLocalStorage() {
        return new HystrixRequestLocalStorage();
    }

    @Bean
    public LocalStorageLifeCycle localStorageLifeCycle() {
        return new HystrixLocalStorageCycle();
    }


    /**
     * 支持hystrix使用线程隔离时依然能够进行跑线程传递GrayRequest
     *
     * @return DefaultHystrixRibbonConnectionPoint
     */
//    @Bean
//    public RibbonConnectionPoint ribbonConnectionPoint(
//            GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
//        return new DefaultHystrixRibbonConnectionPoint(grayManager, requestLocalStorage);
//    }


//    @Bean
//    public GrayTrackFilter grayTrackFilter(
//            GrayTrackHolder grayTrackHolder,
//            RequestLocalStorage requestLocalStorage) {
//        return new GrayTrackFilter(grayTrackHolder, requestLocalStorage) {
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//                if (!HystrixRequestContext.isCurrentThreadInitialized()) {
//                    HystrixRequestContext.initializeContext();
//                }
//                try {
//                    super.doFilter(request, response, chain);
//                } finally {
//                    if (HystrixRequestContext.isCurrentThreadInitialized()) {
//                        HystrixRequestContext.getContextForCurrentThread().shutdown();
//                    }
//                }
//            }
//        };
//    }
}
