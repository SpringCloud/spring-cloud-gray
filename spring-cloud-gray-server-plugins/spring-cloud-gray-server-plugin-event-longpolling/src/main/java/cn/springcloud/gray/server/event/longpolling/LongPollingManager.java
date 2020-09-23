package cn.springcloud.gray.server.event.longpolling;

import cn.springcloud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-02-03 22:13
 */
public interface LongPollingManager {


    /**
     * 将消息推送到client实例
     *
     * @param grayEvent
     * @return 推送client实例的个数
     */
    int sendEvent(GrayEvent grayEvent);

    void mount(ClientLongPolling clientLongPolling);

    void unmount(ClientLongPolling clientLongPolling);

    /**
     * 检查客户端传递过来的超时时间(ms)，如果不符合，就返回默认的超时时间(ms)
     * 不符合的有:1、值为null， 2、小于某个值
     *
     * @param clientTimeout
     * @return
     */
    long getTimeout(Long clientTimeout);
}
