package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePoliciesFO {
    @NotEmpty(message = "实例id不能为空")
    @ApiModelProperty("实例id")
    private String[] instanceIds;
    @NotEmpty(message = "策略id不能为空")
    @ApiModelProperty("策略id")
    private Long[] policyIds;
}
