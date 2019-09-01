package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

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
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
