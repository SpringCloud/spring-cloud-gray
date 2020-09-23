package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-01-31 17:16
 */
public interface GrayEventLogger {

    void log(Object eventSource, TriggerType triggerType, GrayEvent grayEvent);


    @Slf4j
    public static class Default implements GrayEventLogger {

        @Override
        public void log(Object eventSource, TriggerType triggerType, GrayEvent grayEvent) {
            log.info("triggerType:{}, eventMsg:{}, eventSource:{}", triggerType, eventSource, grayEvent);
        }
    }
}
