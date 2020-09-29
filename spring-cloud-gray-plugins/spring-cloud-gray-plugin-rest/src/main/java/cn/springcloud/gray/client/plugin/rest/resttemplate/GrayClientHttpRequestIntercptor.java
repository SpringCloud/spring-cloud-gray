package cn.springcloud.gray.client.plugin.rest.resttemplate;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.commons.GrayRequestHelper;
import cn.springcloud.gray.constants.RequestInterceptorConstants;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.response.http.HttpResponseMessage;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.utils.WebUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;


/**
 * 用于@LoadBalance 标记的 RestTemplate，主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class GrayClientHttpRequestIntercptor implements ClientHttpRequestInterceptor {

    public static final String GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST = "restTemplate.request";

    private GrayRequestProperties grayRequestProperties;
    private RoutingConnectionPoint routingConnectionPoint;

    public GrayClientHttpRequestIntercptor(
            GrayRequestProperties grayRequestProperties, RoutingConnectionPoint routingConnectionPoint) {
        this.grayRequestProperties = grayRequestProperties;
        this.routingConnectionPoint = routingConnectionPoint;
    }

    @SneakyThrows
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

        grayRequest.setAttachment(GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST, request);
        RoutingConnectPointContext connectPointContext = RoutingConnectPointContext.builder()
                .interceptroType(RequestInterceptorConstants.INTERCEPTRO_TYPE_RESTTEMPLATE)
                .grayRequest(grayRequest).build();

        GrayRequestHelper.setPreviousServerInfoToHttpHeaderByInstanceLocalInfo(grayRequest);

        return routingConnectionPoint.executeOrMock(
                connectPointContext, () -> execution.execute(request, body), this::mockResultConvert);

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


    protected ClientHttpResponse mockResultConvert(Object message) {
        HttpResponseMessage httpResponseMessage = HttpResponseMessage.toHttpResponseMessage(message);
        HttpStatus httpStatus = HttpStatus.valueOf(httpResponseMessage.getStatusCode());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(httpResponseMessage.getHeaders().toMap());
        byte[] bodyBytes = httpResponseMessage.getBodyBytes();
        if (Objects.isNull(bodyBytes)) {
            bodyBytes = new byte[0];
        }
        InputStream bodyStream = new ByteArrayInputStream(bodyBytes);
        return new SimpleClientHttpResponse(httpStatus, httpHeaders, bodyStream);
    }
}
