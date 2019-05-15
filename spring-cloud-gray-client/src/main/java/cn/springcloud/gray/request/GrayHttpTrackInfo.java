package cn.springcloud.gray.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Getter
public class GrayHttpTrackInfo extends GrayTrackInfo {

    public static final String GRAY_TRACK_HEADER_PREFIX = GRAY_TRACK_PREFIX + "header";

    public static final String GRAY_TRACK_METHOD = GRAY_TRACK_PREFIX + "method";

    public static final String GRAY_TRACK_PARAMETER_PREFIX = GRAY_TRACK_PREFIX + "param";

    public static final String GRAY_TRACK_URI = GRAY_TRACK_PREFIX + "uri";

    private HttpHeaders headers = new HttpHeaders();
    @Setter
    private String method;
    private MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    @Setter
    private String uri;

    public void addHeader(String name, String value) {
        headers.add(name, value);
    }

    public void setHeader(String name, List<String> values) {
        headers.put(name, values);
    }


    public void addParameter(String name, String value) {
        parameters.add(name, value);
    }

    public void setParameters(String name, List<String> value) {
        parameters.put(name, value);
    }


}
