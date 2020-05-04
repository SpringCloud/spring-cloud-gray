package cn.springcloud.gray.communication.http;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-02-04 21:59
 */
public interface HttpAgent {


    default HttpResult httpGet(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException {
        HttpRequest request = new HttpRequest(path, HttpMethod.GET)
                .setHeaders(headers)
                .setParamValues(paramValues)
                .setEncoding(encoding)
                .setReadTimeoutMs(readTimeoutMs);
        return request(request);
    }


    default HttpResult httpPost(String path, HttpHeaders headers, HttpParams paramValues, String body, String encoding, long readTimeoutMs) throws IOException {
        HttpRequest request = new HttpRequest(path, HttpMethod.POST)
                .setHeaders(headers)
                .setParamValues(paramValues)
                .setEncoding(encoding)
                .setBody(body)
                .setReadTimeoutMs(readTimeoutMs);
        return request(request);
    }


    default HttpResult httpDelete(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException {
        HttpRequest request = new HttpRequest(path, HttpMethod.DELETE)
                .setHeaders(headers)
                .setParamValues(paramValues)
                .setEncoding(encoding)
                .setReadTimeoutMs(readTimeoutMs);
        return request(request);
    }


    HttpResult request(HttpRequest request) throws IOException;

}
