package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;
import org.springframework.core.Ordered;

/**
 * @author saleson
 * @date 2020-01-30 23:29
 */
public interface EventConverter<SOURCE, E extends GrayEvent> extends Ordered {

    E convert(SOURCE source, TriggerType triggerType);


    default E decorate(E event) {
        return event;
    }

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1000;
    }
}
