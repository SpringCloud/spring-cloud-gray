package cn.springcloud.gray.server.event.longpolling;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.server.GrayEventSender;

/**
 * @author saleson
 * @date 2020-02-03 21:16
 */
public class LongPollingGrayEventSender implements GrayEventSender {

    private LongPollingManager longPollingManager;

    public LongPollingGrayEventSender(LongPollingManager longPollingManager) {
        this.longPollingManager = longPollingManager;
    }

    @Override
    public void send(GrayEvent grayEvent) {
        longPollingManager.sendEvent(grayEvent);
    }
}
