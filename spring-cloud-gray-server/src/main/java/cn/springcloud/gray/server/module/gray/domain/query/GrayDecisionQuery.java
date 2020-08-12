package cn.springcloud.gray.server.module.gray.domain.query;

import cn.springcloud.gray.server.module.domain.DelFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-03-16 23:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrayDecisionQuery {
    @NotNull(message = "policyId不能为空")
    private Long policyId;
    @ApiModelProperty("是否删除")
    private DelFlag delFlag = DelFlag.UNDELETE;
}
