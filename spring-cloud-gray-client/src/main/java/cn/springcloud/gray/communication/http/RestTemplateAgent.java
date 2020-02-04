package cn.springcloud.gray.communication.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-04 22:43
 */
public class RestTemplateAgent implements HttpAgent {

    private final String baseUrl;
    private RestTemplate rest;


    public RestTemplateAgent(String baseUrl) {
        this(baseUrl, new RestTemplate());
    }


    public RestTemplateAgent(String baseUrl, int timeout) {
        this.baseUrl = baseUrl;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) timeout);
        requestFactory.setReadTimeout((int) timeout);
        this.rest = new RestTemplate(requestFactory);
    }

    public RestTemplateAgent(String baseUrl, RestTemplate rest) {
        this.baseUrl = baseUrl;
        this.rest = rest;
    }


    @Override
    public HttpResult httpGet(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException {
        String url = getCompleteUrl(path, paramValues, encoding);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.putAll(headers.toMap());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return toHttpResult(responseEntity);
    }

    @Override
    public HttpResult httpPost(String path, HttpHeaders headers, HttpParams paramValues, String body, String encoding, long readTimeoutMs) throws IOException {
        String url = getCompleteUrl(path, paramValues, encoding);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.putAll(headers.toMap());
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return toHttpResult(responseEntity);
    }

    @Override
    public HttpResult httpDelete(String path, HttpHeaders headers, HttpParams paramValues, String encoding, long readTimeoutMs) throws IOException {
        String url = getCompleteUrl(path, paramValues, encoding);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.putAll(headers.toMap());
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        return toHttpResult(responseEntity);
    }

    @Override
    public HttpResult request(HttpRequest request) {
        String url = getCompleteUrl(request.getPath(), request.getParamValues(), request.getEncoding());
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(request.getBody(), httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(
                url, HttpMethod.resolve(request.getMethod().name()), httpEntity, String.class);
        return toHttpResult(responseEntity);
    }


    private HttpResult toHttpResult(ResponseEntity<String> responseEntity) {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(responseEntity.getStatusCode().value());
        httpResult.setHeaders(responseEntity.getHeaders());
        httpResult.setContent(responseEntity.getBody());
        return httpResult;
    }

    private String getCompleteUrl(String path, HttpParams paramValues, String encoding) {
        StringBuilder url = new StringBuilder();
        url.append(baseUrl).append(path);
        if (!Objects.isNull(paramValues)) {
            url.append("?").append(paramValues.encodingParams(encoding));
        }
        return url.toString();
    }
}
