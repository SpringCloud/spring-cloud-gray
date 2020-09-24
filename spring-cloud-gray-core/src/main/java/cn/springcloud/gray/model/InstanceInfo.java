package cn.springcloud.gray.model;

import lombok.*;

import java.io.Serializable;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceInfo implements Serializable {
    private static final long serialVersionUID = -8611566841323195850L;
    private String serviceId;
    private String instanceId;
    private String[] aliases;
    private String host;
    private int port;

    private InstanceStatus instanceStatus;
}
