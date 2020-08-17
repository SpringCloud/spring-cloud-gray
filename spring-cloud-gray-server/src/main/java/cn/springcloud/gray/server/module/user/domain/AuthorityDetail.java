package cn.springcloud.gray.server.module.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-21 21:48
 */
@ApiModel
@Data
public class AuthorityDetail {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "角色")
    private String role;
    @ApiModelProperty(value = "资源")
    private String resource;
    @ApiModelProperty(value = "资源权限")
    private String[] authorities;
    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;


    public void reset(AuthorityDetail other) {
        setOperator(other.getOperator());
        setOperateTime(other.getOperateTime());
        setDelFlag(other.getDelFlag());
        setRole(other.getRole());
        setResource(other.getResource());
        setAuthorities(other.getAuthorities());
    }
}
