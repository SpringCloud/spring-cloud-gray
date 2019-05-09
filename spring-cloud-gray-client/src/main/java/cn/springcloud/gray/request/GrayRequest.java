package cn.springcloud.gray.request;


import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class GrayRequest {

    public static final String ATTRIBUTE_NAME_HTTP_METHOD = "HTTP_METHOD";
    public static final String ATTRIBUTE_NAME_HTTP_HEADER = "HTTP_HEADER";
    public static final String ATTRIBUTE_NAME_HTTP_BODY = "HTTP_BODY";


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

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

}
