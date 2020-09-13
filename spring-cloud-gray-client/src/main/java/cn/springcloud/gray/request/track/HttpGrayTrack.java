package cn.springcloud.gray.request.track;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-09-14 00:19
 */
public interface HttpGrayTrack extends GrayTrack {


    void addHeader(String name, String value);

    void setHeader(String name, List<String> values);

    List<String> getHeader(String name);

    void addParameter(String name, String value);

    void setParameters(String name, List<String> value);

    List<String> getParameter(String name);

    Set<String> headerNames();

    Set<String> parameterNames();

    Map<String, List<String>> getHeaders();

    Map<String, List<String>> getParameters();

    void setUri(String url);

    String getUri();

    void setMethod(String method);

    String getMethod();

}
