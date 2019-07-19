package cn.springcloud.gray.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrayHttpTrackInfo extends GrayTrackInfo {

    public static final String GRAY_TRACK_HEADER_PREFIX = GRAY_TRACK_PREFIX + "header";

    public static final String GRAY_TRACK_METHOD = GRAY_TRACK_PREFIX + "method";

    public static final String GRAY_TRACK_PARAMETER_PREFIX = GRAY_TRACK_PREFIX + "param";

    public static final String GRAY_TRACK_URI = GRAY_TRACK_PREFIX + "uri";

    private HttpHeaders headers = new HttpHeaders();
    @Setter
    @Getter
    private String method;
    private MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    @Setter
    @Getter
    private String uri;

    public void addHeader(String name, String value) {
        headers.add(name.toLowerCase(), value);
    }

    public void setHeader(String name, List<String> values) {
        headers.put(name.toLowerCase(), values);
    }

    public List<String> getHeader(String name) {
        return headers.get(name.toLowerCase());
    }


    public void addParameter(String name, String value) {
        parameters.add(name.toLowerCase(), value);
    }

    public void setParameters(String name, List<String> value) {
        parameters.put(name.toLowerCase(), value);
    }

    public List<String> getParameter(String name) {
        return parameters.get(name.toLowerCase());
    }

    public Set<String> headerNames() {
        return headers.keySet();
    }

    public Set<String> parameterNames() {
        return parameters.keySet();
    }

    public Map<String, List<String>> getHeaders() {
        return MapUtils.unmodifiableMap(headers);
    }

    public Map<String, List<String>> getParameters() {
        return MapUtils.unmodifiableMap(parameters);
    }


}
