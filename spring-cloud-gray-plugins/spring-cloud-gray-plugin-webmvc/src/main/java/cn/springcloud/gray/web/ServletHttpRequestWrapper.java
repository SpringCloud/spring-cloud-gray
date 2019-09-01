package cn.springcloud.gray.web;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletHttpRequestWrapper implements HttpRequest{

    private HttpServletRequest servletRequest;

    public ServletHttpRequestWrapper(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return servletRequest.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return servletRequest.getHeaderNames();
    }

    @Override
    public String getHeader(String headerName) {
        return servletRequest.getHeader(headerName);
    }

    @Override
    public String getRequestURI() {
        return servletRequest.getRequestURI();
    }

    @Override
    public String[] getParameterValues(String name) {
        return servletRequest.getParameterValues(name);
    }

    @Override
    public String getParameter(String name) {
        return servletRequest.getParameter(name);
    }

    @Override
    public String getRemoteAddr() {
        return servletRequest.getRemoteAddr();
    }

    @Override
    public String getMethod() {
        return servletRequest.getMethod();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        HttpHeaders httpHeaders = new HttpHeaders();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = servletRequest.getHeaders(headerName);
            while (headerValues.hasMoreElements()){
                httpHeaders.add(headerName, headerValues.nextElement());
            }
        }
        return httpHeaders;
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return servletRequest.getParameterMap().entrySet().stream()
                .collect(
                        Collectors.toMap(k->k.getKey(), v-> Arrays.asList(v.getValue())));
    }
}
