package cn.springcloud.gray.web;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;

import javax.servlet.*;
import java.io.IOException;

public class GrayTrackFilter implements Filter {

    private RequestLocalStorage requestLocalStorage;

    private GrayTrackHolder grayTrackHolder;


    public GrayTrackFilter(GrayTrackHolder grayTrackHolder, RequestLocalStorage requestLocalStorage) {
        this.grayTrackHolder = grayTrackHolder;
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        GrayHttpTrackInfo webTrack = new GrayHttpTrackInfo();
//        trackors.forEach(trackor -> trackor.call(webTrack, (HttpServletRequest) request));
        grayTrackHolder.recordGrayTrack(webTrack, request);
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
