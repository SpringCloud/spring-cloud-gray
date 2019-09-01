package cn.springcloud.gray.web;

import org.springframework.http.HttpHeaders;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public interface HttpRequest {

    Map<String, List<String>> getHeaders();

    Enumeration<String> getHeaders(String name);

    Enumeration<String> getHeaderNames();

    String getHeader(String headerName);

    String getRequestURI();

    Map<String, List<String>> getParameters();

    String[] getParameterValues(String name);

    String getParameter(String name);

    String getRemoteAddr();

    String getMethod();
}
