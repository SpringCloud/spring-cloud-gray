package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.response.http.HttpResponseMessage;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-26 14:25
 */
public class GrayMockRoutingZuulFilter extends ZuulFilter {

    private RoutingConnectionPoint routingConnectionPoint;

    public GrayMockRoutingZuulFilter(RoutingConnectionPoint routingConnectionPoint) {
        this.routingConnectionPoint = routingConnectionPoint;
    }

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return Objects.nonNull(RoutingConnectPointContext.getContextLocal());
    }

    @Override
    public Object run() throws ZuulException {
        RoutingConnectPointContext routingConnectPointContext = RoutingConnectPointContext.getContextLocal();
        Object mockResult = routingConnectionPoint.excuteMockHandle(routingConnectPointContext);
        if (Objects.nonNull(mockResult)) {
            sendMockResult(mockResult);
        }
        return null;
    }


    private void sendMockResult(Object mockResult) {
        HttpResponseMessage httpResponseMessage = HttpResponseMessage.toHttpResponseMessage(mockResult);
        RequestContext ctx = RequestContext.getCurrentContext();

        ctx.setResponseStatusCode(httpResponseMessage.getStatusCode());
        httpResponseMessage.getHeaders().toMap()
                .entrySet()
                .forEach(entry -> {
                    ctx.getZuulRequestHeaders().put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
                });
        ctx.setResponseBody(httpResponseMessage.getBodyContent());
        ctx.setSendZuulResponse(true);
    }
}
