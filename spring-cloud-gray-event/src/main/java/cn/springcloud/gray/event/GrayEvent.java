package cn.springcloud.gray.event;

import cn.springcloud.gray.event.server.TriggerType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author saleson
 * @date 2020-01-30 12:41
 */
@Data
public abstract class GrayEvent implements Serializable {

    private static final long serialVersionUID = -1040335939179513841L;
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
