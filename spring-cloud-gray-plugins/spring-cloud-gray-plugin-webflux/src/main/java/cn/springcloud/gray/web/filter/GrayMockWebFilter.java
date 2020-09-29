package cn.springcloud.gray.web.filter;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.mock.GrayReuqestMockInfo;
import cn.springcloud.gray.mock.MockManager;
import cn.springcloud.gray.model.HandleRuleType;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.response.http.HttpResponseMessage;
import cn.springcloud.gray.web.utils.WebFluxGrayHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Slf4j
public class GrayMockWebFilter implements WebFilter {

    private RequestLocalStorage requestLocalStorage;

    private MockManager mockManager;


    public GrayMockWebFilter(RequestLocalStorage requestLocalStorage, MockManager mockManager) {
        this.requestLocalStorage = requestLocalStorage;
        this.mockManager = mockManager;
    }

    @Override
    public Mono filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!mockManager.isEnableMock(HandleRuleType.MOCK_APPLICATION_RESPONSE.code())) {
            return chain.filter(exchange);
        }

        GrayClientHolder.getLocalStorageLifeCycle().initContext(GrayMockWebFilter.class.getName());
        try {
            GrayTrackInfoHolder grayTrackInfoHolder = getGrayTrackInfo(exchange);
            if (Objects.isNull(grayTrackInfoHolder)) {
                return chain.filter(exchange);
            }

            GrayHttpRequest grayHttpRequest = createGrayHttpRequest(exchange.getRequest());
            grayHttpRequest.setGrayTrackInfo(grayTrackInfoHolder.getGrayTrackInfo());

            GrayReuqestMockInfo mockInfo = GrayReuqestMockInfo.builder()
                    .grayRequest(grayHttpRequest)
                    .build();
            Object mockResult = mockManager.predicateAndExcuteMockHandle(HandleRuleType.MOCK_APPLICATION_RESPONSE.code(), mockInfo);
            if (Objects.isNull(mockResult)) {
                return chain.filter(exchange);
            }
            HttpResponseMessage httpResponseMessage = HttpResponseMessage.toHttpResponseMessage(mockResult);


            httpResponseMessage.getHeaders().toMap().forEach((n, v) -> {
                exchange.getResponse().getHeaders().addAll(n, v);
            });


            ServerHttpResponse response = exchange.getResponse();
            DataBuffer buffer = response.bufferFactory().wrap(httpResponseMessage.getBodyBytes());
//            return response.writeWith(Mono.just(buffer));
            return response.writeWith(Flux.just(buffer));
        } finally {
            GrayClientHolder.getLocalStorageLifeCycle().closeContext(GrayMockWebFilter.class.getName());
        }
    }

    private GrayTrackInfoHolder getGrayTrackInfo(ServerWebExchange exchange) {
        GrayTrackInfo trackInfo = requestLocalStorage.getGrayTrackInfo();
        if (Objects.nonNull(trackInfo)) {
            return new GrayTrackInfoHolder(trackInfo, false);
        }

        trackInfo = WebFluxGrayHelper.getWebExchangAttribute(exchange, GrayTrackWebFilter.GRAY_WEB_TRACK_ATTR_NAME);
        if (Objects.isNull(trackInfo)) {
            return null;
        }
        requestLocalStorage.setGrayTrackInfo(trackInfo);
        return new GrayTrackInfoHolder(trackInfo, true);
    }

    private GrayHttpRequest createGrayHttpRequest(ServerHttpRequest request) {
        GrayHttpRequest grayHttpRequest = new GrayHttpRequest();
        WebFluxGrayHelper.setHttpServerRequestInfoToGrayHttpRequest(request, grayHttpRequest);
        return grayHttpRequest;
    }

    @Data
    @AllArgsConstructor
    private static class GrayTrackInfoHolder {
        private GrayTrackInfo grayTrackInfo;
        private boolean needRemoveLocalStorage;
    }

}
