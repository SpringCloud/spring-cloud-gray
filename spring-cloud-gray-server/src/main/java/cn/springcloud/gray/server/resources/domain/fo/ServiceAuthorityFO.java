package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ServiceAuthorityFO {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("服务id")
    private String serviceId;
}
