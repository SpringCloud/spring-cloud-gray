package cn.springcloud.gray.model;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceInfo {
    private String serviceId;
    private String instanceId;
    private String[] aliases;
    private String host;
    private int port;

    private InstanceStatus instanceStatus;
}
