package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-17 12:15
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class GrayPolicyDecision {
    @ApiModelProperty("策略")
    private GrayPolicy grayPolicy;
    @ApiModelProperty("决策")
    private List<GrayDecision> grayDecisions;
}
