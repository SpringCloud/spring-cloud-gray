package cn.springcloud.gray.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

@Getter
@Setter
public class GrayHttpRequest extends GrayRequest {

    private Map<String, List<String>> headers = new LinkedMultiValueMap<>();
    private String method;
    private Map<String, List<String>> parameters = new LinkedMultiValueMap<>();
    private byte[] body;


    public void addHeaders(Map<String, ? extends Collection<String>> headers) {
        if (MapUtils.isEmpty(headers)) {
            return;
        }
        headers.forEach((k, v) -> {
            this.headers.put(k, new ArrayList<>(v));
        });
    }


    public void addParameters(Map<String, ? extends Collection<String>> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return;
        }
        parameters.forEach((k, v) -> {
            this.parameters.put(k, new ArrayList<>(v));
        });
    }


    public void addHeader(String name, String value) {
        List<String> values = headers.computeIfAbsent(name, (k) -> new LinkedList());
        values.add(value);
    }


    public List<String> getHeader(String name) {
        return headers.get(name.toLowerCase());
    }


    public List<String> getParameter(String name) {
        return parameters.get(name.toLowerCase());
    }
}
