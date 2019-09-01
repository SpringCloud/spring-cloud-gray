package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.web.HttpRequest;

public interface HttpGrayInfoTracker extends GrayInfoTracker<GrayHttpTrackInfo, HttpRequest> {
}
