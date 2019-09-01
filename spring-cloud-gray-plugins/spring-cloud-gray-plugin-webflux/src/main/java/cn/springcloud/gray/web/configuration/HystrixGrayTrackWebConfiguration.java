package cn.springcloud.gray.web.configuration;

import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.filter.GrayTrackWebFilter;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@ConditionalOnProperty(value = "gray.request.track.enabled", matchIfMissing = true)
public class HystrixGrayTrackWebConfiguration {


    @Bean
    @ConditionalOnProperty(value = "gray.client.runenv", havingValue = "web", matchIfMissing = true)
    @Order(999)
    public GrayTrackWebFilter grayTrackFilter(
            GrayTrackHolder grayTrackHolder,
            RequestLocalStorage requestLocalStorage) {

        return new GrayTrackWebFilter(requestLocalStorage, grayTrackHolder) {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                if (!HystrixRequestContext.isCurrentThreadInitialized()) {
                    HystrixRequestContext.initializeContext();
                }
                try {
                    return super.filter(exchange, chain);
                } finally {
                    if (HystrixRequestContext.isCurrentThreadInitialized()) {
                        HystrixRequestContext.getContextForCurrentThread().shutdown();
                    }
                }
            }
        };
    }
}
