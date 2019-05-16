package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpMethodGrayInfoTracker implements HttpGrayInfoTracker {

    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        trackInfo.setMethod(request.getMethod());
        log.debug("记录下方法:{}", request.getMethod());
    }

    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpServletRequest> args) {
        call(args.getTrackInfo(), args.getRequest());
    }
}
