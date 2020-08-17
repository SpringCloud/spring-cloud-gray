package cn.springcloud.gray.response;

/**
 * @author saleson
 * @date 2020-05-26 12:28
 */
public interface ResponseMessage<BODY> {

    byte[] getBodyBytes();

    String getBodyContent();

    void setBody(BODY body);

    BODY getBody();

}
