package cn.springcloud.gray.response;

/**
 * @author saleson
 * @date 2020-05-17 15:56
 */
public interface ResponseMessage<BODY> {

    byte[] getBodyBytes();

    String getBodyContent();

    void setBody(BODY body);

    BODY getBody();

}
