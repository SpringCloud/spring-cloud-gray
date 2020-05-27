package cn.springcloud.gray.response.http;

import cn.springcloud.gray.http.HttpHeaders;
import cn.springcloud.gray.response.ResponseMessage;
import lombok.ToString;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-17 16:06
 */
@ToString
public class HttpResponseMessage<BODY> implements ResponseMessage<BODY> {

    private int statusCode = 200;
    private HttpHeaders headers = new HttpHeaders();
    private BODY body;


    @Override
    public byte[] getBodyBytes() {
        String body = getBodyContent();
        return Objects.isNull(body) ? null : body.getBytes();
    }

    @Override
    public String getBodyContent() {
        BODY body = getBody();
        return Objects.isNull(body) ? null : body.toString();
    }

    @Override
    public void setBody(BODY body) {
        this.body = body;
    }

    @Override
    public BODY getBody() {
        return body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public static <BODY> HttpResponseMessage<BODY> toHttpResponseMessage(Object obj) {
        if (obj instanceof ResponseMessage) {
            return toHttpResponseMessage((ResponseMessage) obj);
        }
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage();
        httpResponseMessage.setBody(obj);
        return httpResponseMessage;
    }

    public static <BODY> HttpResponseMessage<BODY> toHttpResponseMessage(ResponseMessage<BODY> responseMessage) {
        if (responseMessage instanceof HttpResponseMessage) {
            return (HttpResponseMessage) responseMessage;
        }
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage();
        httpResponseMessage.setBody(responseMessage.getBody());
        return httpResponseMessage;
    }
}
