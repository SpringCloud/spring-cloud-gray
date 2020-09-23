package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-08-16 04:01
 */
public interface GrayEventObserver {

    void observe(GrayEventObserveState observeState, GrayEvent grayEvent);

}
