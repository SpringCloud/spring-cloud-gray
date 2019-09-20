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

    public static final String GRAY_WEB_TRACK_ATTR_NAME = "GrayWebTrack";

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

        recordGrayTrack(exchange, webTrack);
        return chain.filter(exchange).doFinally(t -> requestLocalStorage.removeGrayTrackInfo());
    }

    /**
     * 支持reactor nio event loop
     *
     * @param exchange
     * @param webTrack
     */
    private void recordGrayTrack(ServerWebExchange exchange, GrayHttpTrackInfo webTrack) {
        ////todo 需优化
        exchange.getAttributes().put(GRAY_WEB_TRACK_ATTR_NAME, webTrack);
    }
}
