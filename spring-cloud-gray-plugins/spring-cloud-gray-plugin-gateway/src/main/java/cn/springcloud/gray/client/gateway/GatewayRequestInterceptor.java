package cn.springcloud.gray.client.gateway;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.GrayTrackRecordDevice;
import cn.springcloud.gray.request.GrayTrackRecordHelper;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class GatewayRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return "gateway";
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayTrackInfo grayTrack = request.getGrayTrackInfo();
        if (grayTrack != null) {
            ServerHttpRequest.Builder requestBuilder = (ServerHttpRequest.Builder) request.getAttachment(
                    GrayLoadBalancerClientFilter.GRAY_REQUEST_ATTRIBUTE_GATEWAY_HTTPREQUEST_BUILDER);
            GrayTrackRecordHelper.recordHttpTrack(new GatewayGrayTrackRecordDevice(requestBuilder), grayTrack);
        }
        return true;
    }


    public static class GatewayGrayTrackRecordDevice implements GrayTrackRecordDevice {

        private ServerHttpRequest.Builder requestBuilder;

        public GatewayGrayTrackRecordDevice(ServerHttpRequest.Builder requestBuilder) {
            this.requestBuilder = requestBuilder;
        }

        @Override
        public void record(String name, String value) {
            requestBuilder.header(name, value);
        }

        @Override
        public void record(String name, List<String> values) {
            for (String v : values) {
                requestBuilder.header(name, v);
            }
        }
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }
}
