package cn.springcloud.bamboo.zuul.filter;

import cn.springcloud.bamboo.BambooAppContext;
import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.ConnectPointContext;
import cn.springcloud.bamboo.autoconfig.properties.BambooProperties;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class BambooPreZuulFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(BambooPreZuulFilter.class);

    private BambooProperties bambooProperties;

    public BambooPreZuulFilter(BambooProperties bambooProperties) {
        this.bambooProperties = bambooProperties;
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
        BambooRequest.Builder builder = BambooRequest.builder()
                .serviceId((String)context.get(FilterConstants.SERVICE_ID_KEY))
                .uri((String)context.get(FilterConstants.REQUEST_URI_KEY))
                .ip(context.getZuulRequestHeaders().get(FilterConstants.X_FORWARDED_FOR_HEADER.toLowerCase()))
                .addMultiParams(context.getRequestQueryParams())
                .addHeaders(context.getZuulRequestHeaders())
                .addHeaders(context.getOriginResponseHeaders().stream().collect(Collectors.toMap(Pair::first, Pair::second)));
        context.getOriginResponseHeaders().forEach(pair-> builder.addHeader(pair.first(), pair.second()));

        if(bambooProperties.getBambooRequest().isLoadBody()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(context.getRequest().getInputStream()));
                byte[] reqBody = IOUtils.toByteArray(reader, Charset.forName("UTF-8"));
                builder.requestBody(reqBody);
            } catch (IOException e) {
                String errorMsg = "获取request body出现异常";
                log.error(errorMsg, e);
                throw new RuntimeException(errorMsg, e);
            }
        }

        ConnectPointContext connectPointContext = ConnectPointContext.builder().bambooRequest(builder.build()).build();

        BambooAppContext.getBambooRibbonConnectionPoint().executeConnectPoint(connectPointContext);
        return null;
    }

}
