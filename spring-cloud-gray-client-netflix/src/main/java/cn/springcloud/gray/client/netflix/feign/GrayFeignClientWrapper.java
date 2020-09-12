package cn.springcloud.gray.client.netflix.feign;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.commons.GrayRequestHelper;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.response.http.HttpResponseMessage;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.utils.WebUtils;
import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

class GrayFeignClientWrapper implements Client {

    private Client delegate;
    private RoutingConnectionPoint routingConnectionPoint;
    private GrayRequestProperties grayRequestProperties;

    public GrayFeignClientWrapper(Client delegate, RoutingConnectionPoint routingConnectionPoint, GrayRequestProperties grayRequestProperties) {
        this.delegate = delegate;
        this.routingConnectionPoint = routingConnectionPoint;
        this.grayRequestProperties = grayRequestProperties;
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

        grayRequest.setAttachment(GrayFeignClient.GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST, request);
        grayRequest.setAttachment(GrayFeignClient.GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST_OPTIONS, options);
        RoutingConnectPointContext connectPointContext = RoutingConnectPointContext.builder()
                .interceptroType(GrayNetflixClientConstants.INTERCEPTRO_TYPE_FEIGN)
                .grayRequest(grayRequest).build();

        GrayRequestHelper.setPreviousServerInfoToHttpHeaderByInstanceLocalInfo(grayRequest);

        return routingConnectionPoint.executeOrMock(
                connectPointContext,
                () -> delegate.execute(request, options),
                result -> mockResultConvert(request, result));

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

    public Client getTargetClient() {
        return delegate;
    }


    RoutingConnectionPoint getRoutingConnectionPoint() {
        return routingConnectionPoint;
    }

    GrayRequestProperties getGrayRequestProperties() {
        return grayRequestProperties;
    }

    private Response mockResultConvert(Request request, Object mockResult) {
        HttpResponseMessage httpResponseMessage = HttpResponseMessage.toHttpResponseMessage(mockResult);
        byte[] bodyBytes = httpResponseMessage.getBodyBytes();
        if (Objects.isNull(bodyBytes)) {
            bodyBytes = new byte[0];
        }

        Map headers = httpResponseMessage.getHeaders().toMap();
        return Response.builder()
                .body(bodyBytes)
                .status(httpResponseMessage.getStatusCode())
                .headers(headers)
                .request(request)
                .build();
    }
}
