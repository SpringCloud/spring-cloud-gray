package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
public class HttpMethodGrayTracker implements HttpGrayTracker {

    @Override
    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        trackInfo.setMethod(request.getMethod());
        log.debug("记录下方法:{}", request.getMethod());
    }
}
