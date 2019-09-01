package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * 做一些善后工作。比如删除BambooRequestContext在ThreadLocal中的信息。
 */
public class GrayPostZuulFilter extends ZuulFilter {


    private RoutingConnectionPoint routingConnectionPoint;


    public GrayPostZuulFilter(RoutingConnectionPoint routingConnectionPoint) {
        this.routingConnectionPoint = routingConnectionPoint;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RoutingConnectPointContext cpc = RoutingConnectPointContext.getContextLocal();
        if (cpc != null) {
            routingConnectionPoint.shutdownconnectPoint(cpc);
        }
        return null;
    }
}
