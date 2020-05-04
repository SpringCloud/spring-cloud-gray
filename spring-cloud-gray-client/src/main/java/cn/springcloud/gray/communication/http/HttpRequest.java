package cn.springcloud.gray.communication.http;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author saleson
 * @date 2020-02-04 23:09
 */
@Data
@Accessors(chain = true)
public class HttpRequest {
    private final String path;
    private final HttpMethod method;
    private HttpHeaders headers;
    private HttpParams paramValues;
    private String encoding = "UTF-8";
    private String body;
    private long readTimeoutMs;


    public HttpParams initHttpParams() {
        this.paramValues = new HttpParams();
        return paramValues;
    }


    public HttpHeaders initHttpHeaders() {
        this.headers = new HttpHeaders();
        return headers;
    }
}
