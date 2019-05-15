package cn.springcloud.gray.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GrayTrackInfo {

    public static final String GRAY_TRACK_SEPARATE = "__";

    public static final String GRAY_TRACK_PREFIX = "_graytrack_";

    public static final String GRAY_TRACK_TRACE_IP = GRAY_TRACK_PREFIX + "traceIP";

    public static final String GRAY_TRACK_ATTRIBUTE_PREFIX = GRAY_TRACK_PREFIX + "attr";

    @Setter
    private String traceIp;

    private Map<String, String> attributes = new HashMap<>(32);

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

}
