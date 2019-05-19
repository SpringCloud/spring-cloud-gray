package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.model.InstanceStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RemoteInstanceStatusUpdateFO {
    private String serviceId;
    private String instanceId;
    private InstanceStatus instanceStatus;
}
