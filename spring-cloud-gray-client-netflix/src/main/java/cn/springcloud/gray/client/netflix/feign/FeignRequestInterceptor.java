package cn.springcloud.gray.client.netflix.feign;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.client.netflix.constants.GrayNetflixClientConstants;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import feign.Request;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

@Deprecated
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return GrayNetflixClientConstants.INTERCEPTRO_TYPE_FEIGN;
    }

    @Override
    public boolean pre(GrayRequest request) {
        Request feignRequest = (Request) (request.getAttachment(GrayFeignClient.GRAY_REQUEST_ATTRIBUTE_NAME_FEIGN_REQUEST));
        GrayTrackInfo grayTrack = request.getGrayTrackInfo();
        if (grayTrack != null) {
            if (StringUtils.isNotEmpty(grayTrack.getUri())) {
                feignRequest.headers().put(GrayTrackInfo.GRAY_TRACK_URI, Arrays.asList(grayTrack.getUri()));
            }
            if (StringUtils.isNotEmpty(grayTrack.getTraceIp())) {
                feignRequest.headers().put(GrayTrackInfo.GRAY_TRACK_TRACE_IP, Arrays.asList(grayTrack.getTraceIp()));
            }
            if (StringUtils.isNotEmpty(grayTrack.getMethod())) {
                feignRequest.headers().put(GrayTrackInfo.GRAY_TRACK_METHOD, Arrays.asList(grayTrack.getMethod()));
            }
            if (grayTrack.getParameters() != null && !grayTrack.getParameters().isEmpty()) {
                grayTrack.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    feignRequest.headers().put(name, entry.getValue());
                });
            }
            if (grayTrack.getHeaders() != null && !grayTrack.getHeaders().isEmpty()) {
                grayTrack.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    feignRequest.headers().put(name, entry.getValue());
                });
            }

            Map<String, String> grayAttributes = grayTrack.getAttributes();
            if (MapUtils.isNotEmpty(grayAttributes)) {
                grayAttributes.entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    feignRequest.headers().put(name, Arrays.asList(entry.getValue()));
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
