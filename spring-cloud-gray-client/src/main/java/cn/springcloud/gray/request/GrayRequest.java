package cn.springcloud.gray.request;


import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class GrayRequest {

    @Setter
    @Getter
    private String serviceId;

    @Setter
    @Getter
    private URI uri;

    @Setter
    @Getter
    private GrayTrackInfo grayTrackInfo;


    private Map<String, Object> attributes = new HashMap<>(32);

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

}
