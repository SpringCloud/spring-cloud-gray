package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.client.config.properties.GrayRequestProperties;
import cn.springcloud.gray.client.netflix.connectionpoint.ConnectPointContext;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Enumeration;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class GrayPreZuulFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(GrayPreZuulFilter.class);

    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST = "zuul.request";
    public static final String GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST_CONTEXT = "zuul.requestContext";

    private GrayRequestProperties grayRequestProperties;
    private RibbonConnectionPoint ribbonConnectionPoint;

    public GrayPreZuulFilter(GrayRequestProperties grayRequestProperties, RibbonConnectionPoint ribbonConnectionPoint) {
        this.grayRequestProperties = grayRequestProperties;
        this.ribbonConnectionPoint = ribbonConnectionPoint;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest servletRequest = context.getRequest();

        GrayHttpRequest grayRequest = new GrayHttpRequest();
        URI uri = URI.create((String) context.get(FilterConstants.REQUEST_URI_KEY));
        grayRequest.setUri(uri);
        grayRequest.setServiceId((String) context.get(FilterConstants.SERVICE_ID_KEY));
        grayRequest.addParameters(context.getRequestQueryParams());
        if (grayRequestProperties.isLoadBody()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(context.getRequest().getInputStream()));
                byte[] reqBody = IOUtils.toByteArray(reader);
                grayRequest.setBody(reqBody);
            } catch (IOException e) {
                String errorMsg = "获取request body出现异常";
                log.error(errorMsg, e);
            }
        }

        grayRequest.setMethod(servletRequest.getMethod());
        grayRequest.setHeaders(getHeaders(context));
        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST, servletRequest);
        grayRequest.setAttribute(GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST_CONTEXT, context);
        //context.getZuulRequestHeaders().get(FilterConstants.X_FORWARDED_FOR_HEADER.toLowerCase())

        ConnectPointContext connectPointContext = ConnectPointContext.builder()
                .interceptroType(GrayNetflixClientConstants.INTERCEPTRO_TYPE_ZUUL)
                .grayRequest(grayRequest).build();
        ribbonConnectionPoint.executeConnectPoint(connectPointContext);
        return null;
    }


    private MultiValueMap<String, String> getHeaders(RequestContext context) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        context.getZuulRequestHeaders().entrySet().forEach(entry -> {
            headers.add(entry.getKey(), entry.getValue());
        });
        HttpServletRequest servletRequest = context.getRequest();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, servletRequest.getHeader(headerName));
        }
        return headers;
    }

}
