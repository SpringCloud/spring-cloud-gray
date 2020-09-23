package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.codec.GrayEventEncoder;
import cn.springcloud.gray.event.server.GrayEventLogger;
import cn.springcloud.gray.event.server.TriggerType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-01-31 22:04
 */
@Slf4j
public class GrayEventStorage implements GrayEventLogger {

    private GrayEventLogModule grayEventLogModule;
    private GrayEventEncoder<String> grayEventEncoder;

    public GrayEventStorage(GrayEventLogModule grayEventLogModule, GrayEventEncoder<String> grayEventEncoder) {
        this.grayEventLogModule = grayEventLogModule;
        this.grayEventEncoder = grayEventEncoder;
    }

    @Override
    public void log(Object eventSource, TriggerType triggerType, GrayEvent grayEvent) {
        GrayEventLog grayEventLog = new GrayEventLog();
        grayEventLog.setSourceId(grayEvent.getSourceId());
        grayEventLog.setType(grayEvent.getType());
        grayEventLog.setSortMark(grayEvent.getSortMark());
//        grayEventLog.setEventClass(grayEvent.getClass().getName());
        try {
            grayEventLog.setContent(grayEventEncoder.encode(grayEvent));
            grayEventLogModule.persist(grayEventLog);
        } catch (IOException e) {
            log.error("存储GrayEvent失败", e);
        }

    }
}
