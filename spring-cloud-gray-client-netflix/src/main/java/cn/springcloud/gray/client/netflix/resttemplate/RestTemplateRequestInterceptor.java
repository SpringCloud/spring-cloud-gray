package cn.springcloud.gray.client.netflix.resttemplate;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

public class RestTemplateRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return GrayNetflixClientConstants.INTERCEPTRO_TYPE_RESTTEMPLATE;
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayHttpTrackInfo grayTrack = (GrayHttpTrackInfo) request.getGrayTrackInfo();
        if (grayTrack != null) {
            HttpRequest httpRequest = (HttpRequest) request.getAttribute(
                    GrayClientHttpRequestIntercptor.GRAY_REQUEST_ATTRIBUTE_RESTTEMPLATE_REQUEST);
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            if (StringUtils.isNotEmpty(grayTrack.getUri())) {
                httpHeaders.add(GrayHttpTrackInfo.GRAY_TRACK_URI, grayTrack.getUri());
            }
            if (StringUtils.isNotEmpty(grayTrack.getTraceIp())) {
                httpHeaders.add(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, grayTrack.getTraceIp());
            }
            if (StringUtils.isNotEmpty(grayTrack.getMethod())) {
                httpHeaders.add(GrayHttpTrackInfo.GRAY_TRACK_METHOD, grayTrack.getMethod());
            }
            if (grayTrack.getParameters() != null && !grayTrack.getParameters().isEmpty()) {
                grayTrack.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    httpHeaders.put(name, entry.getValue());
                });
            }
            if (grayTrack.getHeaders() != null && !grayTrack.getHeaders().isEmpty()) {
                grayTrack.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    httpHeaders.put(name, entry.getValue());
                });
            }
        }
        return true;
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }
}
