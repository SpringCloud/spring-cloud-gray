package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayInfoTracker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
public class HttpParameterGrayTracker implements HttpGrayTracker {

    private String[] names;

    public HttpParameterGrayTracker(String[] names) {
        this.names = names;
    }

    @Override
    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        for (String name : names) {
            String[] values = request.getParameterValues(name);
            if (ArrayUtils.isNotEmpty(values)) {
                trackInfo.setParameters(name, Arrays.asList(values));
                log.debug("记录下parameter:{} -> {}", name, values);
            }
        }
    }
}
