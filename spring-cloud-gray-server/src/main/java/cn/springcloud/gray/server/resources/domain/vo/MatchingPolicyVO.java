package cn.springcloud.gray.server.resources.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 00:43
 */
@Data
@ApiModel
public class MatchingPolicyVO {
    @ApiModelProperty("策略Id")
    private Long policyId;
    @ApiModelProperty("策略名称")
    private String policyName;
}
