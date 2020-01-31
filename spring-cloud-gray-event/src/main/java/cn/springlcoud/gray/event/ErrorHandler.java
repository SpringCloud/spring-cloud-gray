package cn.springlcoud.gray.event;

/**
 * @author saleson
 * @date 2020-01-30 15:24
 */
public interface ErrorHandler {


    void handleError(Throwable t);
}
