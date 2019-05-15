package cn.springcloud.gray.web;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.RequestLocalStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class GrayTrackFilter implements Filter {

    private RequestLocalStorage requestLocalStorage;

    private List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors;


    public GrayTrackFilter(RequestLocalStorage requestLocalStorage, List<GrayInfoTracker<GrayHttpTrackInfo, HttpServletRequest>> trackors) {
        this.requestLocalStorage = requestLocalStorage;
        this.trackors = trackors;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        GrayHttpTrackInfo webTrack = new GrayHttpTrackInfo();
        trackors.forEach(trackor -> trackor.call(webTrack, (HttpServletRequest) request));
        requestLocalStorage.setGrayTrackInfo(webTrack);
        try {
            chain.doFilter(request, response);
        } finally {
            requestLocalStorage.removeGrayTrackInfo();
        }
    }

    @Override
    public void destroy() {

    }
}
