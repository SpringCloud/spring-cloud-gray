package cn.springcloud.gray.event;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-30 15:32
 */
public class TGrayInstanceEvent extends GrayEvent {
    public TGrayInstanceEvent(long timestamp, Object source) {
        super(timestamp, source);
    }
}
