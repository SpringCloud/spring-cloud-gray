package cn.springcloud.bamboo.feign;

import cn.springcloud.bamboo.BambooAppContext;
import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.ConnectPointContext;
import cn.springcloud.bamboo.autoconfig.properties.BambooProperties;
import cn.springcloud.bamboo.utils.WebUtils;
import cn.springcloud.bamboo.web.RequestIpKeeper;
import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;
import java.net.URI;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class BambooFeignClient implements Client {

    private Client delegate;
    private BambooProperties bambooProperties;

    public BambooFeignClient(BambooProperties bambooProperties, Client delegate) {
        this.delegate = delegate;
        this.bambooProperties = bambooProperties;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        URI uri = URI.create(request.url());
        BambooRequest.Builder builder = BambooRequest.builder()
                .serviceId(uri.getHost())
                .uri(uri.getPath())
                .ip(RequestIpKeeper.getRequestIp())
                .addMultiParams(WebUtils.getQueryParams(uri.getQuery()));
        if(bambooProperties.getBambooRequest().isLoadBody()){
            builder.requestBody(request.body());
        }


        request.headers().entrySet().forEach(entry ->{
            for (String v : entry.getValue()) {
                builder.addHeader(entry.getKey(), v);
            }
        });

        ConnectPointContext connectPointContext = ConnectPointContext.builder().bambooRequest(builder.build()).build();

        try {
            BambooAppContext.getBambooRibbonConnectionPoint().executeConnectPoint(connectPointContext);
            return delegate.execute(request, options);
        }finally {
            BambooAppContext.getBambooRibbonConnectionPoint().shutdownconnectPoint();
        }
    }
}
