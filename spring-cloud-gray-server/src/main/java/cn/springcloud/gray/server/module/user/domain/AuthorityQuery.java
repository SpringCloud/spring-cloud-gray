package cn.springcloud.gray.server.module.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-03-21 22:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AuthorityQuery {
    @ApiModelProperty(value = "角色")
    private String role;
    @ApiModelProperty(value = "资源")
    private String resource;
    @ApiModelProperty(value = "是否删除, 为空时默认查询全部")
    private Boolean delFlag;
}
