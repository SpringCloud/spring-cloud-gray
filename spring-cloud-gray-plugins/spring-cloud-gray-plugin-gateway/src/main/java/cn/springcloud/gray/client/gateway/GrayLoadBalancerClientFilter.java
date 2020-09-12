package cn.springcloud.gray.client.gateway;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.web.filter.GrayTrackWebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Slf4j
public class GrayLoadBalancerClientFilter extends LoadBalancerClientFilter {

    public static final String GRAY_REQUEST_ATTRIBUTE_GATEWAY_HTTPREQUEST_BUILDER = "gateway.httpRequest.builder";

    @Autowired
    private GrayRequestProperties grayRequestProperties;
    @Autowired
    private RoutingConnectionPoint routingConnectionPoint;
    @Autowired
    private RequestLocalStorage requestLocalStorage;

    private LoadBalancerProperties properties;

    public GrayLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
        this.properties = properties;
    }


    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null
                || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        addOriginalRequestUrl(exchange, url);

        log.trace("LoadBalancerClientFilter url before: " + url);
        // gray append start
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        final ServiceInstance instance = choose(url, exchange, requestBuilder);
        // gray append end

        if (instance == null) {
            throw NotFoundException.create(properties.isUse404(),
                    "Unable to find instance for " + url.getHost());
        }

        URI uri = exchange.getRequest().getURI();

        // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
        // if the loadbalancer doesn't provide one.
        String overrideScheme = instance.isSecure() ? "https" : "http";
        if (schemePrefix != null) {
            overrideScheme = url.getScheme();
        }

        URI requestUrl = loadBalancer.reconstructURI(
                new DelegatingServiceInstance(instance, overrideScheme), uri);

        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);

        // gray append start
        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        // gray append end
        return chain.filter(newExchange);
    }


    protected ServiceInstance choose(URI url, ServerWebExchange exchange, ServerHttpRequest.Builder requestBuilder) {
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

        grayRequest.setAttachment(GRAY_REQUEST_ATTRIBUTE_GATEWAY_HTTPREQUEST_BUILDER, requestBuilder);
        GrayTrackInfo grayTrackInfo = (GrayTrackInfo) exchange.getAttributes().get(GrayTrackWebFilter.GRAY_WEB_TRACK_ATTR_NAME);
        if (grayTrackInfo != null) {
            requestLocalStorage.setGrayTrackInfo(grayTrackInfo);
        }

        RoutingConnectPointContext connectPointContext = RoutingConnectPointContext.builder()
                .interceptroType("gateway")
                .grayRequest(grayRequest).build();

//        final ServiceInstance instance = choose(exchange);

        return routingConnectionPoint.execute(
                connectPointContext, () -> choose(exchange), grayCxt -> requestLocalStorage.removeGrayTrackInfo());


    }

    class DelegatingServiceInstance implements ServiceInstance {
        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            String scheme = delegate.getScheme();
            if (scheme != null) {
                return scheme;
            }
            return this.overrideScheme;
        }

    }
}
