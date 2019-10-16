package com.springcloud.gray.gateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfiguration {


    @Bean
    public KeyResolver uriKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }


//    @Bean
//    public RequestLocalStorage requestLocalStorage() {
//        return new RequestLocalStorage() {
//
//            private FastThreadLocal<GrayTrackInfo> trackThreadLocal = new FastThreadLocal<>();
//            private FastThreadLocal<GrayRequest> reqThreadLocal = new FastThreadLocal<>();
//
//            @Override
//            public void setGrayTrackInfo(GrayTrackInfo grayTrackInfo) {
//                trackThreadLocal.set(grayTrackInfo);
//            }
//
//            @Override
//            public void removeGrayTrackInfo() {
//                trackThreadLocal.remove();
//            }
//
//            @Override
//            public GrayTrackInfo getGrayTrackInfo() {
//                return trackThreadLocal.get();
//            }
//
//            @Override
//            public void setGrayRequest(GrayRequest grayRequest) {
//                reqThreadLocal.set(grayRequest);
//            }
//
//            @Override
//            public void removeGrayRequest() {
//                reqThreadLocal.remove();
//            }
//
//            @Override
//            public GrayRequest getGrayRequest() {
//                return reqThreadLocal.get();
//            }
//        };
//    }

}
