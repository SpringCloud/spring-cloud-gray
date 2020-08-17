package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayDecision {

    private Long id;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("灰度决策名称")
    private String name;
    @ApiModelProperty("决策参数")
    private String infos;
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
