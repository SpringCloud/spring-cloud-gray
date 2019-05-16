package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
public class HttpParameterGrayInfoTracker implements HttpGrayInfoTracker {


    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpServletRequest> args) {
        GrayHttpTrackInfo trackInfo = args.getTrackInfo();
        HttpServletRequest request = args.getRequest();
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
