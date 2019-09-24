package cn.springcloud.gray.client.gateway;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.web.filter.GrayTrackWebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;


public class GrayGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GrayGlobalFilter.class);

    public static final String GRAY_REQUEST_ATTRIBUTE_GATEWAY_HTTPREQUEST_BUILDER = "gateway.httpRequest.builder";

    protected Integer filterOrder = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 10;

    private GrayRequestProperties grayRequestProperties;
    private RoutingConnectionPoint routingConnectionPoint;
    private RequestLocalStorage requestLocalStorage;

    public GrayGlobalFilter(
            GrayRequestProperties grayRequestProperties,
            RoutingConnectionPoint routingConnectionPoint,
            RequestLocalStorage requestLocalStorage) {
        this.grayRequestProperties = grayRequestProperties;
        this.routingConnectionPoint = routingConnectionPoint;
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.setUri(serverHttpRequest.getURI());


        grayRequest.setServiceId(url.getHost().toLowerCase());
        grayRequest.addParameters(serverHttpRequest.getQueryParams());
        if (grayRequestProperties.isLoadBody()) {
            grayRequest.setBody(serverHttpRequest.getBody().blockFirst().asByteBuffer().array());
        }

        grayRequest.setMethod(serverHttpRequest.getMethodValue());
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(serverHttpRequest.getHeaders());
        grayRequest.setHeaders(headers);

        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_GATEWAY_HTTPREQUEST_BUILDER, requestBuilder);

        recordGrayTrack(exchange);

        RoutingConnectPointContext connectPointContext = RoutingConnectPointContext.builder()
                .interceptroType("gateway")
                .grayRequest(grayRequest).build();


        routingConnectionPoint.executeConnectPoint(connectPointContext);

        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange).doFinally(t -> destoryRequestLocal(t, connectPointContext));
    }


    /**
     * 支持reactor nio event loop
     *
     * @param exchange
     */
    private void recordGrayTrack(ServerWebExchange exchange) {
        //todo 需优化
        if (requestLocalStorage.getGrayTrackInfo() == null) {
            GrayTrackInfo grayTrackInfo = (GrayTrackInfo) exchange.getAttributes().get(GrayTrackWebFilter.GRAY_WEB_TRACK_ATTR_NAME);
            if (grayTrackInfo != null) {
                requestLocalStorage.setGrayTrackInfo(grayTrackInfo);
            }
        }
    }


    private void destoryRequestLocal(SignalType t, RoutingConnectPointContext connectPointContext) {
        //todo 需优化
        routingConnectionPoint.shutdownconnectPoint(connectPointContext);
        requestLocalStorage.removeGrayTrackInfo();
    }


    @Override
    public int getOrder() {
        return filterOrder;
    }
}
