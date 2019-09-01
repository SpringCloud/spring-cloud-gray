package cn.springcloud.gray.web.filter;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.ServerHttpRequestWrapper;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;



public class GrayTrackWebFilter implements WebFilter {

    private RequestLocalStorage requestLocalStorage;

    private GrayTrackHolder grayTrackHolder;


    public GrayTrackWebFilter(RequestLocalStorage requestLocalStorage, GrayTrackHolder grayTrackHolder) {
        this.requestLocalStorage = requestLocalStorage;
        this.grayTrackHolder = grayTrackHolder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        GrayHttpTrackInfo webTrack = new GrayHttpTrackInfo();
        grayTrackHolder.recordGrayTrack(webTrack, new ServerHttpRequestWrapper(exchange.getRequest()));
        requestLocalStorage.setGrayTrackInfo(webTrack);
//        try {
            return chain.filter(exchange).doFinally(t->requestLocalStorage.removeGrayTrackInfo());
//        } finally {
//            ;
//        }
    }
}
