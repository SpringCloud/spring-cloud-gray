package cn.springcloud.gray.communication.http;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-02-04 22:00
 */
@Data
public class HttpResult {

    public int code;
    public Map<String, List<String>> headers;
    public String content;
}
