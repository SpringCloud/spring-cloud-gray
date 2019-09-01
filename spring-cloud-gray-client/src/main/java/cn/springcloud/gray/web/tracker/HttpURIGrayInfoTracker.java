package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.web.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpURIGrayInfoTracker implements HttpGrayInfoTracker {


    public void call(GrayHttpTrackInfo trackInfo, HttpRequest request) {
        trackInfo.setUri(request.getRequestURI());
        log.debug("记录下uri:{}", trackInfo.getUri());
    }


    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpRequest> args) {
        call(args.getTrackInfo(), args.getRequest());
    }
}
