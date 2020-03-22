package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-03-21 23:16
 */
@Data
@ApiModel
public class AuthorityDetailFO {

    @ApiModelProperty(value = "角色", required = true)
    private String role;
    @ApiModelProperty(value = "资源", required = true)
    private String resource;
    @ApiModelProperty(value = "资源权限", required = true)
    private String[] authorities;
    @ApiModelProperty(value = "是否删除", required = true)
    private Boolean delFlag;
}
