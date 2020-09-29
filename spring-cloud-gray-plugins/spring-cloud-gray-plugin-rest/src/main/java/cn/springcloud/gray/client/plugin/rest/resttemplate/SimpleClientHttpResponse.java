package cn.springcloud.gray.client.plugin.rest.resttemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author saleson
 * @date 2020-05-26 11:15
 */
public class SimpleClientHttpResponse extends AbstractClientHttpResponse {

    private HttpStatus httpStatus;

    private HttpHeaders httpHeaders;

    private InputStream bodyStream;

    public SimpleClientHttpResponse(HttpStatus httpStatus, HttpHeaders httpHeaders, InputStream bodyStream) {
        this.httpStatus = httpStatus;
        this.httpHeaders = httpHeaders;
        this.bodyStream = bodyStream;
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return httpStatus.value();
    }

    @Override
    public String getStatusText() throws IOException {
        return httpStatus.getReasonPhrase();
    }

    @Override
    public void close() {
        if (this.bodyStream != null) {
            try {
                StreamUtils.drain(this.bodyStream);
                this.bodyStream.close();
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return bodyStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }
}
