package cn.springcloud.gray.client.plugin.openfeign;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.GrayTrackRecordDevice;
import cn.springcloud.gray.request.GrayTrackRecordHelper;
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
        GrayTrackInfo grayTrack = getGrayTrackInfo(template);
        if (grayTrack == null) {
            return;
        }
        GrayTrackRecordHelper.recordHttpTrack(new FeignGrayTrackRecordDevice(template), grayTrack);
    }

    private GrayTrackInfo getGrayTrackInfo(RequestTemplate template) {
        try {
            return requestLocalStorage.getGrayTrackInfo();
        } catch (Exception e) {
            log.warn("从requestLocalStorage中获取GrayTrackInfo对象失败, url:{}", template.url(), e);
            return null;
        }
    }


    public static class FeignGrayTrackRecordDevice implements GrayTrackRecordDevice {

        private RequestTemplate template;

        public FeignGrayTrackRecordDevice(RequestTemplate template) {
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

}
