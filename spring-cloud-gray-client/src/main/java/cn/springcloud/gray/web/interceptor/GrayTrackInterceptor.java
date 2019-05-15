package cn.springcloud.gray.web.interceptor;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/** {@link cn.springcloud.gray.web.GrayTrackFilter} */
public class GrayTrackInterceptor extends HandlerInterceptorAdapter {

  private RequestLocalStorage requestLocalStorage;

  private List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors;

  public GrayTrackInterceptor(
      RequestLocalStorage requestLocalStorage,
      List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors) {
    this.trackors = trackors;
    this.requestLocalStorage = requestLocalStorage;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    GrayHttpTrackInfo webTrack = new GrayHttpTrackInfo();
    trackors.forEach(trackor -> trackor.call(webTrack, request));
    requestLocalStorage.setGrayTrackInfo(webTrack);
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    requestLocalStorage.removeGrayTrackInfo();
    super.afterCompletion(request, response, handler, ex);
  }
}
