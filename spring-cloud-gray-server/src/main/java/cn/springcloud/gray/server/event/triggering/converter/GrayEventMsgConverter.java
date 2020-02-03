package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.event.GrayEventMsg;
import cn.springlcoud.gray.event.GrayEvent;
import cn.springlcoud.gray.event.server.EventConverter;
import cn.springlcoud.gray.event.server.TriggerType;
import org.springframework.core.Ordered;

/**
 * @author saleson
 * @date 2020-02-01 18:16
 */
public class GrayEventMsgConverter implements EventConverter<GrayEventMsg, GrayEvent> {


    @Override
    public GrayEvent convert(GrayEventMsg eventMsg, TriggerType triggerType) {

        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
