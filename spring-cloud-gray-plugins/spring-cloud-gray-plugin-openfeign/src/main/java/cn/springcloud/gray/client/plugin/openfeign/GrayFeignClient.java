package cn.springcloud.gray.client.plugin.openfeign;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import feign.Client;
import feign.Request;
import feign.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.URI;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class GrayFeignClient implements Client, ApplicationContextAware {


    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST = "feign.request";
    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST_OPTIONS = "feign.request.options";


    private Client delegate;
    private RoutingConnectionPoint routingConnectionPoint;
    private GrayRequestProperties grayRequestProperties;
    private volatile GrayFeignClientWrapper proxy;
    private ApplicationContext applicationContext;

    public GrayFeignClient(
            Client delegate,
            RoutingConnectionPoint routingConnectionPoint,
            GrayRequestProperties grayRequestProperties) {
        this.delegate = delegate;
        this.routingConnectionPoint = routingConnectionPoint;
        this.grayRequestProperties = grayRequestProperties;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        return getProxyClient(request).execute(request, options);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取代理Client，第一次时创建代理对象。
     *
     * @param request 请求对象
     * @return 代理Client
     */
    private Client getProxyClient(Request request) {
        if (proxy == null) {
            String serviceId = applicationContext.getEnvironment().getProperty("feign.client.name");
            boolean isLoadBalanced = StringUtils.equalsIgnoreCase(URI.create(request.url()).getHost(), serviceId);
            Client delegateClient = delegate;
            if (!isLoadBalanced && delegate instanceof LoadBalancerFeignClient) {
                delegateClient = ((LoadBalancerFeignClient) delegate).getDelegate();
            }
            proxy = new GrayFeignClientWrapper(delegateClient, routingConnectionPoint, grayRequestProperties);

        }
        return proxy;
    }


}
