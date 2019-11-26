package cn.springcloud.gray.exceptions;

/**
 * @author saleson
 * @date 2019-11-25 21:53
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
