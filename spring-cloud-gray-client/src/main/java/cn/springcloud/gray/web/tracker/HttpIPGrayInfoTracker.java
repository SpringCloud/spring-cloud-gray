package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpIPGrayInfoTracker implements HttpGrayInfoTracker {


    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        trackInfo.setTraceIp(WebUtils.getIpAddr(request));
        log.debug("记录下ip:{}", trackInfo.getTraceIp());
    }

    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpServletRequest> args) {
        call(args.getTrackInfo(), args.getRequest());
    }
}
