package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.HttpGrayTrackRecordDevice;
import cn.springcloud.gray.request.HttpGrayTrackRecordHelper;
import com.netflix.zuul.context.RequestContext;

import java.util.List;

public class ZuulRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return GrayNetflixClientConstants.INTERCEPTRO_TYPE_ZUUL;
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayHttpTrackInfo grayTrack = (GrayHttpTrackInfo) request.getGrayTrackInfo();
        if (grayTrack != null) {
            RequestContext context = (RequestContext) request.getAttribute(
                    GrayPreZuulFilter.GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST_CONTEXT);
            HttpGrayTrackRecordHelper.record(new ZuulHttpGrayTrackRecordDevice(context), grayTrack);

//            if (StringUtils.isNotEmpty(grayTrack.getUri())) {
//                context.getZuulRequestHeaders().put(GrayHttpTrackInfo.GRAY_TRACK_URI, grayTrack.getUri());
//            }
//            if (StringUtils.isNotEmpty(grayTrack.getTraceIp())) {
//                context.getZuulRequestHeaders().put(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, grayTrack.getTraceIp());
//            }
//            if (StringUtils.isNotEmpty(grayTrack.getMethod())) {
//                context.getZuulRequestHeaders().put(GrayHttpTrackInfo.GRAY_TRACK_METHOD, grayTrack.getMethod());
//            }
//            if (grayTrack.getParameters() != null && !grayTrack.getParameters().isEmpty()) {
//                grayTrack.getParameters().entrySet().forEach(entry -> {
//                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
//                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
//                            .append(entry.getKey()).toString();
//                    entry.getValue().forEach(v -> {
//                        context.getZuulRequestHeaders().put(name, v);
//                    });
//                });
//            }
//            if (grayTrack.getHeaders() != null && !grayTrack.getHeaders().isEmpty()) {
//                grayTrack.getHeaders().entrySet().forEach(entry -> {
//                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX)
//                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
//                            .append(entry.getKey()).toString();
//                    entry.getValue().forEach(v -> {
//                        context.getZuulRequestHeaders().put(name, v);
//                    });
//                });
//            }
//
//            Map<String, String> grayAttributes = grayTrack.getAttributes();
//            if (MapUtils.isNotEmpty(grayAttributes)) {
//                grayAttributes.entrySet().forEach(entry -> {
//                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX)
//                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
//                            .append(entry.getKey()).toString();
//                    context.getZuulRequestHeaders().put(name, entry.getValue());
//                });
//            }

        }
        return true;
    }


    public static class ZuulHttpGrayTrackRecordDevice implements HttpGrayTrackRecordDevice {

        private RequestContext context;

        public ZuulHttpGrayTrackRecordDevice(RequestContext context) {
            this.context = context;
        }

        @Override
        public void record(String name, String value) {
            context.getZuulRequestHeaders().put(name, value);
        }

        @Override
        public void record(String name, List<String> values) {
            for (String v : values) {
                context.getZuulRequestHeaders().put(name, v);
            }
        }
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }
}
