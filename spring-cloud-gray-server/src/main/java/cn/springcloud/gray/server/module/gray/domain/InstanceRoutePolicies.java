package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePolicies {
    @ApiModelProperty("实例id")
    private String[] instanceIds;
    @ApiModelProperty("策略id")
    private Long[] policyIds;
    @ApiModelProperty("操作人id")
    private String operator;
}
