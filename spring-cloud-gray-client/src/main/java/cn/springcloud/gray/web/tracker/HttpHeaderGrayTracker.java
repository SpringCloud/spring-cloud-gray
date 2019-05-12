package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
public class HttpHeaderGrayTracker implements HttpGrayTracker {

    private String[] headers;

    public HttpHeaderGrayTracker(String[] headers) {
        this.headers = headers;
    }

    @Override
    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        for (String header : headers) {
            Enumeration<String> headerValues = request.getHeaders(header);
            while (headerValues.hasMoreElements()) {
                String value = headerValues.nextElement();
                trackInfo.addHeader(header, value);
                log.debug("记录下header:{} -> {}", header, value);
            }
        }
    }
}
