package cn.springlcoud.gray.event.server;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-08-16 04:01
 */
public interface GrayEventObserver {

    void observe(GrayEventObserveState observeState, GrayEvent grayEvent);

}
