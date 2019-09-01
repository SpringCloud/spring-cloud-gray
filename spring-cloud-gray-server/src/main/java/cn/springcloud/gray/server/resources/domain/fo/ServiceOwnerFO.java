package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel
@Data
public class ServiceOwnerFO {
    @NotEmpty
    @ApiModelProperty("服务id")
    private String serviceId;
    @NotEmpty
    @ApiModelProperty(value = "用户id")
    private String userId;
}
