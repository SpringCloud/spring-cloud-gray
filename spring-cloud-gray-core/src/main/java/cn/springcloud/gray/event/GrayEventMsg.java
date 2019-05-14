package cn.springcloud.gray.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class GrayEventMsg implements Serializable {
    private static final long serialVersionUID = -8114806214567175543L;
    private String serviceId;
    private String instanceId;
    private EventType eventType;

}
