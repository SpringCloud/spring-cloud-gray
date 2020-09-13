package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.web.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class HttpHeaderGrayInfoTracker implements HttpGrayInfoTracker {


    @Override
    public void call(TrackArgs<GrayTrackInfo, HttpRequest> args) {
        GrayTrackInfo trackInfo = args.getTrackInfo();
        HttpRequest request = args.getRequest();
        String defValue = args.getTrackDefinition().getValue();
        if (StringUtils.isEmpty(defValue)) {
            return;
        }

        for (String header : defValue.split(",")) {
            Enumeration<String> headerValues = request.getHeaders(header);
            List<String> values = null;
            if (headerValues instanceof List) {
                values = (List<String>) headerValues;
            } else {
                values = new ArrayList<>();
                while (headerValues.hasMoreElements()) {
                    String value = headerValues.nextElement();
                    values.add(value);
                }
            }
            if (!CollectionUtils.isEmpty(values)) {
                log.debug("记录下header:{} -> {}", header, values);
                trackInfo.setHeader(header, values);
            }
        }
    }
}
