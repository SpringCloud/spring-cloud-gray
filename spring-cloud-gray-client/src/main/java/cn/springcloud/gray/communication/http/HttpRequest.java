package cn.springcloud.gray.communication.http;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-04 23:09
 */
@Data
public class HttpRequest {
    private final String path;
    private final HttpMethod method;
    private HttpHeaders headers;
    private HttpParams paramValues;
    private String encoding = "UTF-8";
    private String body;
    private long readTimeoutMs;
}
