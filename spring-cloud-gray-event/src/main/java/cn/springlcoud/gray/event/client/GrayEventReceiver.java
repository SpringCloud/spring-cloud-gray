package cn.springlcoud.gray.event.client;

/**
 * @author saleson
 * @date 2020-01-30 12:46
 */
public interface GrayEventReceiver {


    void start(long sortMark);


    void shutdown();

}
