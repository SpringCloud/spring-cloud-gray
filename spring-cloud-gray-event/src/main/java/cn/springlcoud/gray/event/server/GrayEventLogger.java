package cn.springlcoud.gray.event.server;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-31 17:16
 */
public interface GrayEventLogger {

    void log(Object eventMsg, TriggerType triggerType, GrayEvent grayEvent);
}
