package cn.springlcoud.gray.event;

import java.util.EventObject;

/**
 * @author saleson
 * @date 2020-01-30 12:41
 */
public abstract class GrayEvent extends EventObject {

    private final long timestamp;

    public GrayEvent(long timestamp, Object source) {
        super(source);
        this.timestamp = timestamp;
    }


    public long getTimestamp() {
        return timestamp;
    }
}
