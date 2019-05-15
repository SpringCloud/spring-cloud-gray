package cn.springcloud.gray.client.netflix.feign;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.connectionpoint.ConnectPointContext;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.utils.WebUtils;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class GrayFeignClient implements Client {


    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST = "feign.request";
    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST_OPTIONS = "feign.request.options";


    private GrayRequestProperties grayRequestProperties;
    private GrayTrackProperties grayTrackProperties;
    private RibbonConnectionPoint ribbonConnectionPoint;
    private Client delegate;

    public GrayFeignClient(
            Client delegate, RibbonConnectionPoint ribbonConnectionPoint, GrayRequestProperties grayRequestProperties) {
        this.delegate = delegate;
        this.grayRequestProperties = grayRequestProperties;
        this.ribbonConnectionPoint = ribbonConnectionPoint;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        URI uri = URI.create(request.url());


        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.setUri(uri);
        grayRequest.setServiceId(uri.getHost());
        grayRequest.setParameters(WebUtils.getQueryParams(uri.getQuery()));
        grayRequest.addHeaders(request.headers());
        grayRequest.setMethod(request.method());

        if (grayRequestProperties.isLoadBody()) {
            grayRequest.setBody(request.body());
        }

        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST, request);
        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST_OPTIONS, options);
        ConnectPointContext connectPointContext = ConnectPointContext.builder()
                .interceptroType(GrayNetflixClientConstants.INTERCEPTRO_TYPE_FEIGN)
                .grayRequest(grayRequest).build();
        return ribbonConnectionPoint.execute(connectPointContext, () -> delegate.execute(request, options));

//        try {
//            ribbonConnectionPoint.executeConnectPoint(connectPointContext);
//            return delegate.execute(request, options);
//        } catch (Exception e) {
//            connectPointContext.setThrowable(e);
//            throw e;
//        } finally {
//            ribbonConnectionPoint.shutdownconnectPoint(connectPointContext);
//        }
    }
}
