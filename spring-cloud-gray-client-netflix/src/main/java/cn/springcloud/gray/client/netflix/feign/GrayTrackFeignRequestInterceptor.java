package cn.springcloud.gray.client.netflix.feign;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;


public class GrayTrackFeignRequestInterceptor implements RequestInterceptor {

    private RequestLocalStorage requestLocalStorage;


    public GrayTrackFeignRequestInterceptor(RequestLocalStorage requestLocalStorage) {
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void apply(RequestTemplate template) {
        GrayHttpTrackInfo grayTrack = (GrayHttpTrackInfo) requestLocalStorage.getGrayTrackInfo();
        if (grayTrack != null) {
            if (StringUtils.isNotEmpty(grayTrack.getUri())) {
                template.header(GrayHttpTrackInfo.GRAY_TRACK_URI, grayTrack.getUri());
            }
            if (StringUtils.isNotEmpty(grayTrack.getTraceIp())) {
                template.header(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, grayTrack.getTraceIp());
            }
            if (StringUtils.isNotEmpty(grayTrack.getMethod())) {
                template.header(GrayHttpTrackInfo.GRAY_TRACK_METHOD, grayTrack.getMethod());
            }
            if (grayTrack.getParameters() != null && !grayTrack.getParameters().isEmpty()) {
                grayTrack.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    template.header(name, entry.getValue());
                });
            }
            if (grayTrack.getHeaders() != null && !grayTrack.getHeaders().isEmpty()) {
                grayTrack.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    template.header(name, entry.getValue());
                });
            }
        }
    }
}
