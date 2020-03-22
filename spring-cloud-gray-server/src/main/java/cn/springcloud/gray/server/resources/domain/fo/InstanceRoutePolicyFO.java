package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePolicyFO {
    @NotEmpty(message = "实例id不能为空")
    @ApiModelProperty("实例id")
    private String instanceId;
    @NotNull(message = "策略id不能为空")
    @ApiModelProperty("策略id")
    private Long policyId;
}
