package cn.springlcoud.gray.event.server;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-30 23:29
 */
public interface EventConverter<SOURCE, E extends GrayEvent> {

    E convert(SOURCE source, TriggerType triggerType);
}
