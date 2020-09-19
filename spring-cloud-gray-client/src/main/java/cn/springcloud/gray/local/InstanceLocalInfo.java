package cn.springcloud.gray.local;

import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceLocalInfo {
    private String serviceId;
    @Setter
    private String instanceId;
    private String host;
    private int port;
    @Setter
    private boolean isGray;

}
