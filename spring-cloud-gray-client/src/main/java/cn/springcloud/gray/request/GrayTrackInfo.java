package cn.springcloud.gray.request;

import cn.springcloud.gray.request.track.HttpGrayTrack;
import lombok.Getter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Getter
public class GrayTrackInfo implements HttpGrayTrack {

    public static final String ATTRIBUTE_TRACE_IP = "track_ip";

    public static final String ATTRIBUTE_ATTR = "attr";

    public static final String GRAY_TRACK_TRACE_IP = GRAY_TRACK_PREFIX + ATTRIBUTE_TRACE_IP;

    public static final String GRAY_TRACK_ATTRIBUTE_PREFIX = GRAY_TRACK_PREFIX + ATTRIBUTE_ATTR;


    public static final String ATTRIBUTE_HTTP_METHOD = "method";
    public static final String ATTRIBUTE_HTTP_URI = "uri";
    public static final String ATTRIBUTE_HTTP_HEADER = "header";
    public static final String ATTRIBUTE_HTTP_PARAMETER = "param";


    public static final String GRAY_TRACK_HEADER_PREFIX = GRAY_TRACK_PREFIX + ATTRIBUTE_HTTP_HEADER;

    public static final String GRAY_TRACK_METHOD = GRAY_TRACK_PREFIX + ATTRIBUTE_HTTP_METHOD;

    public static final String GRAY_TRACK_PARAMETER_PREFIX = GRAY_TRACK_PREFIX + ATTRIBUTE_HTTP_PARAMETER;

    public static final String GRAY_TRACK_URI = GRAY_TRACK_PREFIX + ATTRIBUTE_HTTP_URI;


    private Map<String, String> attributes = new HashMap<>(32);

    private HttpHeaders headers = new HttpHeaders();
    private MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

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

    @Override
    public String getAttribute(String name) {
        return attributes.get(name.toLowerCase());
    }

    @Override
    public void setAttribute(String name, String value) {
        attributes.put(name.toLowerCase(), value);
    }

    @Override
    public Map<String, String> getAttributes() {
        return MapUtils.unmodifiableMap(attributes);
    }

    @Override
    public void setTraceIp(String ip) {
        setAttribute(ATTRIBUTE_TRACE_IP, ip);
    }

    @Override
    public String getTraceIp() {
        return StringUtils.defaultString(getAttribute(ATTRIBUTE_TRACE_IP));
    }

    @Override
    public void addHeader(String name, String value) {
        headers.add(name.toLowerCase(), value);
    }

    @Override
    public void setHeader(String name, List<String> values) {
        headers.put(name.toLowerCase(), values);
    }

    @Override
    public List<String> getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    @Override
    public void addParameter(String name, String value) {
        parameters.add(name.toLowerCase(), value);
    }

    @Override
    public void setParameters(String name, List<String> value) {
        parameters.put(name.toLowerCase(), value);
    }

    @Override
    public List<String> getParameter(String name) {
        return parameters.get(name.toLowerCase());
    }

    @Override
    public Set<String> headerNames() {
        return headers.keySet();
    }

    @Override
    public Set<String> parameterNames() {
        return parameters.keySet();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return MapUtils.unmodifiableMap(headers);
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return MapUtils.unmodifiableMap(parameters);
    }

    @Override
    public void setUri(String url) {
        setAttribute(ATTRIBUTE_HTTP_URI, url);
    }

    @Override
    public String getUri() {
        return StringUtils.defaultString(getAttribute(ATTRIBUTE_HTTP_URI));
    }

    @Override
    public void setMethod(String method) {
        setAttribute(ATTRIBUTE_HTTP_METHOD, method);
    }

    @Override
    public String getMethod() {
        return StringUtils.defaultString(getAttribute(ATTRIBUTE_HTTP_METHOD));
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ATTRIBUTE_ATTR, getAttributes());
        map.put(ATTRIBUTE_HTTP_HEADER, getHeaders());
        map.put(ATTRIBUTE_HTTP_PARAMETER, getParameters());
        return map;
    }

}
