package cn.springcloud.bamboo;

import java.io.UnsupportedEncodingException;

public class BytesRequestBody implements RequestBody{

    private static final String DEFAULT_CHARSET = "UTF-8";

    private byte[] body;

    public BytesRequestBody(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public String getBodyString() {
        return getBodyString(DEFAULT_CHARSET);
    }

    @Override
    public String getBodyString(String charset) {
        try {
            return new String(body, charset);
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
