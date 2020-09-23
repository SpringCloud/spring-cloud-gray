package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.code.component.initializer.SpringInitializer;
import cn.springcloud.gray.event.server.AbstractGrayEventTrigger;
import cn.springcloud.gray.event.server.EventConverter;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-05 12:49
 */


@Slf4j
public class GrayEventInitializer extends SpringInitializer {

    @Override
    protected void initialization() {
        initializeGrayEventTrigger();
    }

    private void initializeGrayEventTrigger() {
        GrayEventTrigger grayEventTrigger = getBean(GrayEventTrigger.class);
        if (Objects.isNull(grayEventTrigger) || !(grayEventTrigger instanceof AbstractGrayEventTrigger)) {
            return;
        }
        AbstractGrayEventTrigger abstractGrayEventTrigger = (AbstractGrayEventTrigger) grayEventTrigger;
        abstractGrayEventTrigger.createEventConverterRetriever(getBeans(EventConverter.class));
    }
}
