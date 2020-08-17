package cn.springcloud.gray.decision;

import cn.springcloud.gray.request.GrayRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class DecisionInputArgs {

    private GrayRequest grayRequest;
    private Map<String, Object> attrs = new HashMap<>();

    public void setAttribute(String name, Object value) {
        attrs.put(name, value);
    }

    public void removeAttribute(String name) {
        attrs.remove(name);
    }

    public <T> T getAttribute(String name) {
        return (T) attrs.get(name);
    }
}
