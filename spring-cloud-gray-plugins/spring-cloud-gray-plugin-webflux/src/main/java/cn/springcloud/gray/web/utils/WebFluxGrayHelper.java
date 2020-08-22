package cn.springcloud.gray.web.utils;

import cn.springcloud.gray.request.GrayHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

/**
 * @author saleson
 * @date 2020-08-22 09:06
 */
public class WebFluxGrayHelper {


    public static void setWebExchangAttribute(ServerWebExchange exchange, String name, Object value) {
        exchange.getAttributes().put(name, value);
    }


    public static <T> T getWebExchangAttribute(ServerWebExchange exchange, String name) {
        exchange.getRequest().getPath().pathWithinApplication().value();
        return (T) exchange.getAttributes().get(name);
    }


    public static void setHttpServerRequestInfoToGrayHttpRequest(ServerHttpRequest serverHttpRequest, GrayHttpRequest grayHttpRequest) {
        grayHttpRequest.setUri(URI.create(serverHttpRequest.getPath().pathWithinApplication().value()));
        grayHttpRequest.setMethod(serverHttpRequest.getMethod().name());
        grayHttpRequest.addParameters(serverHttpRequest.getQueryParams());
        grayHttpRequest.addHeaders(serverHttpRequest.getHeaders());

    }

}
