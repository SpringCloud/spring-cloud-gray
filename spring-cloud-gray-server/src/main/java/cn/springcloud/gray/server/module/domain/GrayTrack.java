package cn.springcloud.gray.server.module.domain;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayTrack {
    private Long id;
    private String serviceId;
    private String instanceId;
    private String name;
    private String infos;
}
