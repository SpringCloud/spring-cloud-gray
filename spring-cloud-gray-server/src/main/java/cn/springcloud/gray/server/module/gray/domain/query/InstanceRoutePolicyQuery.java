package cn.springcloud.gray.server.module.gray.domain.query;

import cn.springcloud.gray.server.module.domain.DelFlag;
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
public class InstanceRoutePolicyQuery {
    @ApiModelProperty("实例id")
    private String instanceId;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("是否删除")
    private DelFlag delFlag;
}
