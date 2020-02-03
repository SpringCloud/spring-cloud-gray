package cn.springlcoud.gray.event;

import cn.springlcoud.gray.event.server.TriggerType;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-01-30 12:41
 */
@Data
public abstract class GrayEvent<S> {

    private long timestamp;

    private long sortMark;

    private TriggerType triggerType;

    private S source;

    public GrayEvent() {
        this.timestamp = System.currentTimeMillis();
    }


    public abstract String getSourceId();

    public String getType() {
        return getClass().getSimpleName();
    }

}
