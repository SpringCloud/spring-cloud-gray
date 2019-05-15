package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Enumeration;

@Slf4j
public class HttpIPGrayTracker implements HttpGrayTracker {


    @Override
    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        trackInfo.setTraceIp(WebUtils.getIpAddr(request));
        log.debug("记录下ip:{}", trackInfo.getTraceIp());
    }
}
