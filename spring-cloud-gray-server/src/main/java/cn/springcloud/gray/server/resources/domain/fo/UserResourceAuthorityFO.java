package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-03-22 00:51
 */
@ApiModel
@Data
public class UserResourceAuthorityFO {
    @NotEmpty(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @NotEmpty(message = "资源类型不能为空")
    @ApiModelProperty(value = "资源类型,例如:namespace,service", example = "namespace", required = true)
    private String resource;
    @NotEmpty(message = "资源id不能为空")
    @ApiModelProperty(value = "资源id", example = "test", required = true)
    private String resourceId;
    @NotNull(message = "权限标记不能为空")
    @ApiModelProperty(value = "权限标记[OWNER,ADMIN]", required = true)
    private ResourceAuthorityFlag authorityFlag;
    @NotNull(message = "是否删除不能为空")
    @ApiModelProperty(value = "是否删除", required = true)
    private Boolean delFlag;
}
