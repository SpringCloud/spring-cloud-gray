package cn.springcloud.gray.communication.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author saleson
 * @date 2020-02-04 22:21
 */
public class HttpParams {
    private List<String> paramValues = new ArrayList<>(32);


    public void addParamPair(String name, String... values) {
        paramValues.add(name);
        for (String value : values) {
            paramValues.add(value);
        }
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


    private String encodeParameter(String param, String encoding) {
        try {
            return URLEncoder.encode(param, encoding);
        } catch (UnsupportedEncodingException e) {

        }
        return param;
    }

    @Override
    public String toString() {
        return encodingParams(Charset.defaultCharset().displayName());
    }
}
