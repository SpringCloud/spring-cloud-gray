package cn.springcloud.gray.communication.http;

import cn.springcloud.gray.http.HttpParams;
import cn.springcloud.gray.http.HttpRequest;
import cn.springcloud.gray.http.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
    public HttpResult request(HttpRequest request) {
//        String url = getCompleteUrl(request.getPath(), request.getParamValues(), request.getEncoding());
//        String url = getCompleteUrl(request.getPath(), request.getParamValues(), null);
        Map<String, String> params = getParams(request.getParamValues());
        String url = getExtendUrl(request.getPath(), params);

        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        if (Objects.nonNull(request.getHeaders())) {
            httpHeaders.putAll(request.getHeaders().toMap());
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(request.getBody(), httpHeaders);

        ResponseEntity<String> responseEntity = rest.exchange(
                url, HttpMethod.resolve(request.getMethod().name()), httpEntity, String.class, params);
        return toHttpResult(responseEntity);
    }


    private HttpResult toHttpResult(ResponseEntity<String> responseEntity) {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(responseEntity.getStatusCode().value());
        httpResult.setHeaders(responseEntity.getHeaders());
        httpResult.setContent(responseEntity.getBody());
        return httpResult;
    }

    private String getExtendUrl(String path, Map<String, ?> params) {
        String fullPath = baseUrl + path;
        List<String> querys = new ArrayList<>(params.size());
        params.forEach((k, v) -> {
            StringBuilder query = new StringBuilder();
            query.append(k).append("={").append(k).append("}");
            querys.add(query.toString());
        });
        String queryString = StringUtils.join(querys, "&");
        if (StringUtils.indexOf(fullPath, "?") > -1) {
            return fullPath + "&" + queryString;
        }
        return fullPath + "?" + queryString;
    }

    private Map<String, String> getParams(HttpParams paramValues) {
        if (Objects.isNull(paramValues)) {
            return Collections.emptyMap();
        }
        return paramValues.toValueMap();
    }

    private String getCompleteUrl(String path, HttpParams paramValues, String encoding) {
        StringBuilder url = new StringBuilder();
        url.append(baseUrl).append(path);
        if (Objects.nonNull(paramValues)) {
            String queryString = StringUtils.isEmpty(encoding) ? paramValues.toQueryString() : paramValues.encodingParams(encoding);
            url.append("?").append(queryString);
        }
        return url.toString();
    }
}
