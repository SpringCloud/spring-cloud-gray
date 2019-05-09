package cn.springcloud.gray.request;

import java.util.HashMap;
import java.util.Map;

public class GrayTrackInfo {


    private Map<String, Object> attributes = new HashMap<>(32);

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }


}
