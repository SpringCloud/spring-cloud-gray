package cn.springcloud.gray.server.module.user.domain;

import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class UserResourceAuthority {
    @ApiModelProperty("id")
    private Long id;
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
    @ApiModelProperty(value = "操作人")
    private String operator;
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    public void reset(UserResourceAuthority other) {
        setUserId(other.getUserId());
        setResource(other.getResource());
        setResourceId(other.getResourceId());
        setAuthorityFlag(other.getAuthorityFlag());
        setDelFlag(other.getDelFlag());
        setOperateTime(other.getOperateTime());
        setOperator(other.getOperator());
    }


//    public String generateId() {
//        String str = new StringBuilder()
//                .append(userId)
//                .append(Constants.UNDERLINE)
//                .append(resource)
//                .append(Constants.UNDERLINE)
//                .append(resourceId)
//                .toString();
//        return Md5Utils.md5(str.toString());
//    }
//
//    public String regenerateId() {
//        this.id = generateId();
//        return this.id;
//    }


}
