package cn.springcloud.gray.client.netflix.zuul;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.constants.RequestInterceptorConstants;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.GrayTrackRecordDevice;
import cn.springcloud.gray.request.GrayTrackRecordHelper;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ZuulRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return RequestInterceptorConstants.INTERCEPTRO_TYPE_ZUUL;
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayTrackInfo grayTrack = request.getGrayTrackInfo();
        if (grayTrack != null) {
            RequestContext context = (RequestContext) request.getAttachment(
                    GrayPreZuulFilter.GRAY_REQUEST_ATTRIBUTE_NAME_ZUUL_REQUEST_CONTEXT);
            GrayTrackRecordHelper.recordHttpTrack(new ZuulGrayTrackRecordDevice(context), grayTrack);
        }
        return true;
    }


    public static class ZuulGrayTrackRecordDevice implements GrayTrackRecordDevice {

        private RequestContext context;

        public ZuulGrayTrackRecordDevice(RequestContext context) {
            this.context = context;
        }

        @Override
        public void record(String name, String value) {
            context.getZuulRequestHeaders().put(name, value);
        }

        @Override
        public void record(String name, List<String> values) {
            context.getZuulRequestHeaders().put(name, StringUtils.join(values, ";"));
//            for (String v : values) {
//                context.getZuulRequestHeaders().put(name, v);
//            }
        }
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }
}
