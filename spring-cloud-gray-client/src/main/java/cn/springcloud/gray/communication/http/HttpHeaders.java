package cn.springcloud.gray.communication.http;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-02-04 22:17
 */
public class HttpHeaders {

    private Map<String, List<String>> headers = new LinkedMultiValueMap<>();


    public void addHeader(String name, String value) {
        List<String> values = headers.computeIfAbsent(name, (k) -> new LinkedList());
        values.add(value);
    }

    public void setHeader(String name, String... values) {
        headers.put(name, new LinkedList(Arrays.asList(values)));
    }


    public Map<String, List<String>> toMap() {
        return MapUtils.unmodifiableMap(headers);
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
