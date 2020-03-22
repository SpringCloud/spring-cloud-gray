package cn.springcloud.gray.server.exception;

/**
 * @author saleson
 * @date 2020-03-17 13:27
 */
public class NonAuthorityException extends RuntimeException {

    public NonAuthorityException() {
        super();
    }

    public NonAuthorityException(String msg) {
        super(msg);
    }
}
