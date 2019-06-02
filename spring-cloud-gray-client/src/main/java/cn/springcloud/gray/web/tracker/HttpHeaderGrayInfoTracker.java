package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class HttpHeaderGrayInfoTracker implements HttpGrayInfoTracker {


    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpServletRequest> args) {
        GrayHttpTrackInfo trackInfo = args.getTrackInfo();
        HttpServletRequest request = args.getRequest();
        String defValue = args.getTrackDefinition().getValue();
        if (StringUtils.isEmpty(defValue)) {
            return;
        }

        for (String header : defValue.split(",")) {
            Enumeration<String> headerValues = request.getHeaders(header);
            List<String> values = new ArrayList<>();
            while (headerValues.hasMoreElements()) {
                String value = headerValues.nextElement();
                values.add(value);
            }
            log.debug("记录下header:{} -> {}", header, values);
            trackInfo.setHeader(header, values);
        }
    }
}
