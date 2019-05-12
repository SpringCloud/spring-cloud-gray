package cn.springcloud.gray.client.netflix.resttemplate;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.netflix.connectionpoint.ConnectPointContext;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.utils.WebUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;


/**
 * 用于@LoadBalance 标记的 RestTemplate，主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class GrayClientHttpRequestIntercptor implements ClientHttpRequestInterceptor {

    public static final String GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST = "restTemplate.request";

    private GrayRequestProperties grayRequestProperties;
    private RibbonConnectionPoint ribbonConnectionPoint;

    public GrayClientHttpRequestIntercptor(
            GrayRequestProperties grayRequestProperties, RibbonConnectionPoint ribbonConnectionPoint) {
        this.grayRequestProperties = grayRequestProperties;
        this.ribbonConnectionPoint = ribbonConnectionPoint;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        URI uri = request.getURI();
        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.setUri(uri);
        grayRequest.setServiceId(uri.getHost());
        grayRequest.setParameters(WebUtils.getQueryParams(uri.getQuery()));
        if (grayRequestProperties.isLoadBody()) {
            grayRequest.setBody(body);
        }
        grayRequest.setMethod(request.getMethod().name());
        grayRequest.addHeaders(request.getHeaders());

        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST, request);
        ConnectPointContext connectPointContext = ConnectPointContext.builder()
                .interceptroType(GrayNetflixClientConstants.INTERCEPTRO_TYPE_RESTTEMPLATE)
                .grayRequest(grayRequest).build();

        return ribbonConnectionPoint.execute(connectPointContext, () -> execution.execute(request, body));

//        try {
//            ribbonConnectionPoint.executeConnectPoint(connectPointContext);
//            return execution.execute(request, body);
//        } catch (Exception e) {
//            connectPointContext.setThrowable(e);
//            throw e;
//        } finally {
//            ribbonConnectionPoint.shutdownconnectPoint(connectPointContext);
//        }
    }
}
