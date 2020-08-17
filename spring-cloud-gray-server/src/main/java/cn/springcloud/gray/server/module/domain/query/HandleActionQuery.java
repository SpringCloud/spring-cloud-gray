package cn.springcloud.gray.server.module.domain.query;

import cn.springcloud.gray.server.module.domain.DelFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-05-30 22:58
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class HandleActionQuery {
    @NotNull(message = "handleId不能为空")
    private Long handleId;

    @ApiModelProperty("是否删除")
    private DelFlag delFlag = DelFlag.UNDELETE;
}
