package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.web.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpMethodGrayInfoTracker implements HttpGrayInfoTracker {

    public void call(GrayTrackInfo trackInfo, HttpRequest request) {
        trackInfo.setMethod(request.getMethod());
        log.debug("记录下方法:{}", request.getMethod());
    }

    @Override
    public void call(TrackArgs<GrayTrackInfo, HttpRequest> args) {
        call(args.getTrackInfo(), args.getRequest());
    }
}
