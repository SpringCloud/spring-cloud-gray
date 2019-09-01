package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayPolicy {

    private Long id;
    @ApiModelProperty("服务实例id")
    private String instanceId;
    @ApiModelProperty("策略别名")
    private String alias;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
