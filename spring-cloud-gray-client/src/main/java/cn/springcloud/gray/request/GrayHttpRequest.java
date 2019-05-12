package cn.springcloud.gray.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GrayHttpRequest extends GrayRequest {

    private Map<String, ? extends Collection<String>> headers = new LinkedMultiValueMap<>();
    private String method;
    private Map<String, ? extends Collection<String>> parameters = new LinkedMultiValueMap<>();
    private byte[] body;


    public void addHeaders(Map<String, ? extends Collection<String>> headers) {
        headers.forEach((k, v) -> {
            Map<String, Collection<String>> headerMap = (Map<String, Collection<String>>) this.headers;
            headerMap.put(k, v);
        });
    }


    public void addParameters(Map<String, ? extends Collection<String>> parameters) {
        parameters.forEach((k, v) -> {
            Map<String, Collection<String>> parameterMap = (Map<String, Collection<String>>) this.parameters;
            parameterMap.put(k, v);
        });
    }


}
