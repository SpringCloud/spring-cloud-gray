package cn.springcloud.gray.client.plugin.rest.resttemplate;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.constants.RequestInterceptorConstants;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.GrayTrackRecordDevice;
import cn.springcloud.gray.request.GrayTrackRecordHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.util.List;

public class RestTemplateRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return RequestInterceptorConstants.INTERCEPTRO_TYPE_RESTTEMPLATE;
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayTrackInfo grayTrack = request.getGrayTrackInfo();
        if (grayTrack != null) {
            HttpRequest httpRequest = (HttpRequest) request.getAttachment(
                    GrayClientHttpRequestIntercptor.GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST);
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            GrayTrackRecordHelper.recordHttpTrack(new RestTemplateGrayTrackRecordDevice(httpHeaders), grayTrack);
        }
        return true;
    }


    public static class RestTemplateGrayTrackRecordDevice implements GrayTrackRecordDevice {

        private HttpHeaders httpHeaders;

        public RestTemplateGrayTrackRecordDevice(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        @Override
        public void record(String name, String value) {
            httpHeaders.set(name, value);
        }

        @Override
        public void record(String name, List<String> values) {
            httpHeaders.put(name, values);
        }
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }
}
