package cn.springcloud.gray.server.discovery;

import cn.springcloud.gray.server.module.domain.InstanceStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstanceInfo {
    private String serviceId;
    private String instanceId;

    private InstanceStatus instanceStatus;
}
