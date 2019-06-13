package cn.springcloud.gray.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class GrayHttpRequest extends GrayRequest {

    private Map<String, ? extends Collection<String>> headers = new LinkedMultiValueMap<>();
    private String method;
    private Map<String, ? extends Collection<String>> parameters = new LinkedMultiValueMap<>();
    private byte[] body;


    public void addHeaders(Map<String, ? extends Collection<String>> headers) {
        if (MapUtils.isEmpty(headers)) {
            return;
        }
        headers.forEach((k, v) -> {
            Map<String, Collection<String>> headerMap = (Map<String, Collection<String>>) this.headers;
            headerMap.put(k, v);
        });
    }


    public void addParameters(Map<String, ? extends Collection<String>> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return;
        }
        parameters.forEach((k, v) -> {
            Map<String, Collection<String>> parameterMap = (Map<String, Collection<String>>) this.parameters;
            parameterMap.put(k, v);
        });
    }


}
