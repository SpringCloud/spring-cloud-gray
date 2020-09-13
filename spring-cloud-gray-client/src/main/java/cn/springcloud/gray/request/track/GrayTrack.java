package cn.springcloud.gray.request.track;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-09-14 00:17
 */
public interface GrayTrack {

    public static final String GRAY_TRACK_SEPARATE = "__";

    public static final String GRAY_TRACK_PREFIX = "_g_t_";


    String getAttribute(String name);

    void setAttribute(String name, String value);

    Map<String, String> getAttributes();

    void setTraceIp(String ip);

    String getTraceIp();

    Map<String, Object> toMap();

}
