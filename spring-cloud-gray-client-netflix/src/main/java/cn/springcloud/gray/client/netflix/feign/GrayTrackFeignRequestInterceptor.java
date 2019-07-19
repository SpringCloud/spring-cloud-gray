package cn.springcloud.gray.client.netflix.feign;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.HttpGrayTrackRecordDevice;
import cn.springcloud.gray.request.HttpGrayTrackRecordHelper;
import cn.springcloud.gray.request.RequestLocalStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class GrayTrackFeignRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrayTrackFeignRequestInterceptor.class);

    private RequestLocalStorage requestLocalStorage;


    public GrayTrackFeignRequestInterceptor(RequestLocalStorage requestLocalStorage) {
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void apply(RequestTemplate template) {
        GrayHttpTrackInfo grayTrack = getGrayHttpTrackInfo(template);
        if (grayTrack == null) {
            return;
        }
        HttpGrayTrackRecordHelper.record(new FeignHttpGrayTrackRecordDevice(template), grayTrack);
//        if (StringUtils.isNotEmpty(grayTrack.getUri())) {
//            template.header(GrayHttpTrackInfo.GRAY_TRACK_URI, grayTrack.getUri());
//        }
//        if (StringUtils.isNotEmpty(grayTrack.getTraceIp())) {
//            template.header(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, grayTrack.getTraceIp());
//        }
//        if (StringUtils.isNotEmpty(grayTrack.getMethod())) {
//            template.header(GrayHttpTrackInfo.GRAY_TRACK_METHOD, grayTrack.getMethod());
//        }
//        if (MapUtils.isNotEmpty(grayTrack.getParameters())) {
//            appendGrayTrackInfosToHeader(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX, grayTrack.getParameters(), template);
////            String prefix = GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX + GrayTrackInfo.GRAY_TRACK_SEPARATE;
////            grayTrack.getParameters().entrySet().forEach(entry -> {
////                template.header(prefix + entry.getKey(), entry.getValue());
////            });
//        }
//        if (MapUtils.isNotEmpty(grayTrack.getHeaders())) {
//            appendGrayTrackInfosToHeader(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX, grayTrack.getHeaders(), template);
////            String prefix = GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX + GrayTrackInfo.GRAY_TRACK_SEPARATE;
////            grayTrack.getHeaders().entrySet().forEach(entry -> {
////                template.header(prefix + entry.getKey(), entry.getValue());
////            });
//        }
//        appendGrayTrackInfoToHeader(GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX, grayTrack.getAttributes(), template);
    }

    private GrayHttpTrackInfo getGrayHttpTrackInfo(RequestTemplate template) {
        try {
            return (GrayHttpTrackInfo) requestLocalStorage.getGrayTrackInfo();
        } catch (Exception e) {
            log.warn("从requestLocalStorage中获取GrayTrackInfo对象失败, url:{}", template.url(), e);
            return null;
        }
    }


    public static class FeignHttpGrayTrackRecordDevice implements HttpGrayTrackRecordDevice {

        private RequestTemplate template;

        public FeignHttpGrayTrackRecordDevice(RequestTemplate template) {
            this.template = template;
        }

        @Override
        public void record(String name, String value) {
            template.header(name, value);
        }

        @Override
        public void record(String name, List<String> values) {
            template.header(name, values);
        }
    }

//
//    private void appendGrayTrackInfoToHeader(String grayPrefix, Map<String, String> infos, RequestTemplate template) {
//        String prefix = grayPrefix + GrayTrackInfo.GRAY_TRACK_SEPARATE;
//        if (MapUtils.isNotEmpty(infos)) {
//            infos.entrySet().forEach(entry -> {
//                template.header(prefix + entry.getKey(), entry.getValue());
//            });
//        }
//    }
//
//    private void appendGrayTrackInfosToHeader(String grayPrefix, Map<String, List<String>> infos, RequestTemplate template) {
//        String prefix = grayPrefix + GrayTrackInfo.GRAY_TRACK_SEPARATE;
//        infos.entrySet().forEach(entry -> {
//            template.header(prefix + entry.getKey(), entry.getValue());
//        });
//    }
}
