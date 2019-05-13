package cn.springcloud.gray;

import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceLocalInfo {
    private String serviceId;
    private String instanceId;
    private String host;
    private int port;
    @Setter
    private boolean isGray;

}
