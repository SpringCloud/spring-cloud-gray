package cn.springcloud.gray.web;

import cn.springcloud.gray.Enumerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;


public class ServerHttpRequestWrapper implements HttpRequest {

    private ServerHttpRequest serverHttpRequest;

    public ServerHttpRequestWrapper(ServerHttpRequest serverHttpRequest) {
        this.serverHttpRequest = serverHttpRequest;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return serverHttpRequest.getHeaders();
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = serverHttpRequest.getHeaders().get(name);
        if(values==null){
            return new Enumerator(ListUtils.EMPTY_LIST);
        }

        return new Enumerator(values);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Enumerator(serverHttpRequest.getHeaders().keySet());
    }

    @Override
    public String getHeader(String headerName) {
        return serverHttpRequest.getHeaders().getFirst(headerName);
    }

    @Override
    public String getRequestURI() {
        return serverHttpRequest.getURI().getPath();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        return serverHttpRequest.getQueryParams();
    }

    @Override
    public String[] getParameterValues(String name) {
        List<String> params = serverHttpRequest.getQueryParams().get(name);
        if(CollectionUtils.isEmpty(params)){
            return new String[0];
        }
        return params.toArray(new String[params.size()]);
    }

    @Override
    public String getParameter(String name) {
        return serverHttpRequest.getQueryParams().getFirst(name);
    }

    @Override
    public String getRemoteAddr() {
        return serverHttpRequest.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public String getMethod() {
        return serverHttpRequest.getMethodValue();
    }
}
