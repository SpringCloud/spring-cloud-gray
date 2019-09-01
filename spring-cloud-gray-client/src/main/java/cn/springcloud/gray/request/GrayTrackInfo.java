package cn.springcloud.gray.request;

import lombok.Getter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class GrayTrackInfo {

    public static final String GRAY_TRACK_SEPARATE = "__";

    public static final String GRAY_TRACK_PREFIX = "_g_t_";

    public static final String ATTRIBUTE_TRACE_IP = "track_ip";

    public static final String GRAY_TRACK_TRACE_IP = GRAY_TRACK_PREFIX + ATTRIBUTE_TRACE_IP;

    public static final String GRAY_TRACK_ATTRIBUTE_PREFIX = GRAY_TRACK_PREFIX + "attr";

    private Map<String, String> attributes = new HashMap<>(32);

    public String getAttribute(String name) {
        return attributes.get(name.toLowerCase());
    }

    public void setAttribute(String name, String value) {
        attributes.put(name.toLowerCase(), value);
    }


    public Map<String, String> getAttributes() {
        return MapUtils.unmodifiableMap(attributes);
    }

    /**
     * 生成灰度追踪的名称
     *
     * @param keys
     * @return
     */
    public static String generateGrayTrackName(String... keys) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            str.append(keys[i]);
            if (!Objects.equals(i + 1, keys.length)) {
                str.append(GRAY_TRACK_SEPARATE);
            }
        }
        return str.toString();
    }

    public void setTraceIp(String ip){
        setAttribute(ATTRIBUTE_TRACE_IP, ip);
    }

    public String getTraceIp(){
        return StringUtils.defaultString(getAttribute(ATTRIBUTE_TRACE_IP));
    }

}
