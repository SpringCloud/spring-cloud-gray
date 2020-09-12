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


    private Map<String, String> attributes = new HashMap<>(32);
    private Map<String, Object> attachments = new HashMap<>();

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }


    public Object getAttachment(String name) {
        return attachments.get(name);
    }

    public void setAttachment(String name, Object value) {
        attachments.put(name, value);
    }


}
