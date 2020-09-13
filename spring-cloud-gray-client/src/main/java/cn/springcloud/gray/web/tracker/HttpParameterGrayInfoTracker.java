package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.web.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Slf4j
public class HttpParameterGrayInfoTracker implements HttpGrayInfoTracker {


    @Override
    public void call(TrackArgs<GrayTrackInfo, HttpRequest> args) {
        GrayTrackInfo trackInfo = args.getTrackInfo();
        HttpRequest request = args.getRequest();
        String defValue = args.getTrackDefinition().getValue();
        if (StringUtils.isEmpty(defValue)) {
            return;
        }
        for (String name : defValue.split(",")) {
            String[] values = request.getParameterValues(name);
            if (ArrayUtils.isNotEmpty(values)) {
                trackInfo.setParameters(name, Arrays.asList(values));
                log.debug("记录下parameter:{} -> {}", name, values);
            }
        }

    }
}
