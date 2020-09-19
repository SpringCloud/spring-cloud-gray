package cn.springcloud.gray.http;

import cn.springcloud.gray.utils.WebUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author saleson
 * @date 2020-02-04 22:21
 */
public class HttpParams {
    private List<String> paramValues = new ArrayList<>(32);


    public HttpParams addParamPair(String name, String value) {
        paramValues.add(name);
        paramValues.add(value);
        return this;
    }

    public String encodingParams(String encoding) {
        StringBuilder sb = new StringBuilder();
        if (null == paramValues) {
            return null;
        }

        for (Iterator<String> iter = paramValues.iterator(); iter.hasNext(); ) {
            sb.append(iter.next()).append("=");
            sb.append(encodeParameter(iter.next(), encoding));
            if (iter.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    public String toQueryString() {
        StringBuilder sb = new StringBuilder();
        if (null == paramValues) {
            return null;
        }

        for (Iterator<String> iter = paramValues.iterator(); iter.hasNext(); ) {
            sb.append(iter.next()).append("=");
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }


    private String encodeParameter(String param, String encoding) {
        try {
            return URLEncoder.encode(param, encoding);
        } catch (UnsupportedEncodingException e) {

        }
        return param;
    }

    @Override
    public String toString() {
        return toQueryString();
    }

    public Map<String, List<String>> toValuesMap() {
        return WebUtils.getQueryParams(toQueryString());
    }

    public Map<String, String> toValueMap() {
        Map<String, String> map = new HashMap<>(paramValues.size() / 2);
        if (null == paramValues) {
            return map;
        }

        for (Iterator<String> iter = paramValues.iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = iter.next();
            map.put(key, value);
        }

        return map;
    }
}
