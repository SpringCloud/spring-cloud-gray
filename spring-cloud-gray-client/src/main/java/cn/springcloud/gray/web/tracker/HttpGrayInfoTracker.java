package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;

import javax.servlet.http.HttpServletRequest;

public interface HttpGrayInfoTracker extends GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest> {
}
