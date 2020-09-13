package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.web.HttpRequest;

public interface HttpGrayInfoTracker extends GrayInfoTracker<GrayTrackInfo, HttpRequest> {

    @Override
    default boolean shold(TrackArgs<GrayTrackInfo, HttpRequest> args) {
        return args.getRequest() instanceof HttpRequest;
    }
}
