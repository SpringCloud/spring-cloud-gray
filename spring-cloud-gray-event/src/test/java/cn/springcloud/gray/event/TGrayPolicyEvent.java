package cn.springcloud.gray.event;

import cn.springlcoud.gray.event.GrayEvent;

/**
 * @author saleson
 * @date 2020-01-30 21:40
 */
public class TGrayPolicyEvent extends GrayEvent {
    public TGrayPolicyEvent(long timestamp, Object source) {
        super(timestamp, source);
    }
}
