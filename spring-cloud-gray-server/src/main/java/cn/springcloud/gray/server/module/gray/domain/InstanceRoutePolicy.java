package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePolicy {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("实例id")
    private String instanceId;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
