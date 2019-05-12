package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpURIGrayTracker implements HttpGrayTracker {


    @Override
    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        trackInfo.setUri(request.getRequestURI());
        log.debug("记录下uri:{}", trackInfo.getUri());
    }
}
