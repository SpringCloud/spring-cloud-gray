package cn.springcloud.gray.web.interceptor;

import cn.springcloud.gray.request.GrayTrackInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GrayTrackInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        GrayTrackInfo webTrack = new GrayTrackInfo();
        //todo

        GrayTrackInfo.setCurrentGrayWebTrack(webTrack);
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {


        GrayTrackInfo.removeCurrentGrayWebTrack();
        super.afterCompletion(request, response, handler, ex);
    }
}
