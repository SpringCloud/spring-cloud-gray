package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.client.netflix.connectionpoint.ConnectPointContext;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * 做一些善后工作。比如删除BambooRequestContext在ThreadLocal中的信息。
 */
public class GrayPostZuulFilter extends ZuulFilter {


    private RibbonConnectionPoint ribbonConnectionPoint;


    public GrayPostZuulFilter(RibbonConnectionPoint ribbonConnectionPoint) {
        this.ribbonConnectionPoint = ribbonConnectionPoint;
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
        ribbonConnectionPoint.shutdownconnectPoint(ConnectPointContext.getContextLocal());
        return null;
    }
}
