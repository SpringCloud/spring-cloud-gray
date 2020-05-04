package cn.springcloud.gray.communication.exception;

/**
 * @author saleson
 * @date 2020-05-04 20:30
 */
public class CommunicationException extends RuntimeException {


    public CommunicationException(String msg, Exception cause) {
        super(msg, cause);
    }

    public CommunicationException(String msg) {
        super(msg);
    }

    public CommunicationException(Exception cause) {
        super(cause);
    }
}
