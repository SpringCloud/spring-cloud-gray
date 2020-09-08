package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author saleson
 * @date 2020-09-09 00:02
 */
@Data
public class GrayInstanceAliasFO {
    @NotEmpty(message = "实例id不能为空")
    @ApiModelProperty(value = "实例id", required = true)
    private String instanceId;
    @ApiModelProperty(value = "别名", required = true)
    private String[] aliases;
}
