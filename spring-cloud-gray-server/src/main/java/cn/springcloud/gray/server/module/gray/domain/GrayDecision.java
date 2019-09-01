package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayDecision {

    private Long id;
    @ApiModelProperty("实例id")
    private String instanceId;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("灰度决策名称")
    private String name;
    @ApiModelProperty("决策参数")
    private String infos;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
