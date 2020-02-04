package cn.springlcoud.gray.event;

import cn.springlcoud.gray.event.server.TriggerType;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-01-30 12:41
 */
@Data
public abstract class GrayEvent {

    private long timestamp;

    private long sortMark;

    private TriggerType triggerType;

    public GrayEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public abstract String getSourceId();

    public String getType() {
        return getClass().getSimpleName();
    }

}
