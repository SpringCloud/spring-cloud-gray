package cn.springcloud.gray.web.filter;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.web.ServerHttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Slf4j
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

        GrayTrackInfo webTrack = new GrayTrackInfo();
        requestLocalStorage.getLocalStorageLifeCycle().initContext();
        try {
            grayTrackHolder.recordGrayTrack(webTrack, new ServerHttpRequestWrapper(exchange.getRequest()));
            requestLocalStorage.setGrayTrackInfo(webTrack);

            recordGrayTrack(exchange, webTrack);
            return chain.filter(exchange).doFinally(t -> {
                requestLocalStorage.clearAll();
            });
        } finally {
            requestLocalStorage.clearAll();
        }
    }

    /**
     * 支持reactor nio event loop
     *
     * @param exchange
     * @param webTrack
     */
    private void recordGrayTrack(ServerWebExchange exchange, GrayTrackInfo webTrack) {
        exchange.getAttributes().put(GRAY_WEB_TRACK_ATTR_NAME, webTrack);
    }
}
