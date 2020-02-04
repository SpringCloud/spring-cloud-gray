package cn.springcloud.gray.communication.http;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-02-04 21:59
 */
public interface HttpAgent {


    HttpResult httpGet(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException;


    HttpResult httpPost(String path, HttpHeaders headers, HttpParams paramValues, String body, String encoding, long readTimeoutMs) throws IOException;


    HttpResult httpDelete(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException;


    HttpResult request(HttpRequest request) throws IOException;

}
