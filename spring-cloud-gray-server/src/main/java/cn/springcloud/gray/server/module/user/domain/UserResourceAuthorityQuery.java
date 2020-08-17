package cn.springcloud.gray.server.module.user.domain;

import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-03-22 00:37
 */
@Data
public class UserResourceAuthorityQuery {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty(value = "资源类型,例如:namespace,service", example = "namespace")
    private String resource;
    @ApiModelProperty(value = "资源id", example = "test")
    private String resourceId;
    @ApiModelProperty(value = "权限标记[OWNER,ADMIN]")
    private ResourceAuthorityFlag authorityFlag;
    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;
}
