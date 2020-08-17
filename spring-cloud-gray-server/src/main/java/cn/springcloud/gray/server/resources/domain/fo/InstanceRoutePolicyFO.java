package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePolicyFO {

    @NotEmpty(message = "namespace不能为空")
    @ApiModelProperty("命名空间")
    private String namespace;
    @NotEmpty(message = "类型不能为空")
    @ApiModelProperty("类型,如service_route, instance_route, service_multi_ver_route")
    private String type;
    @NotEmpty(message = "模块id不能为空")
    @ApiModelProperty("模块id，用于搜索索引，如果serviceId")
    private String moduleId;
    @NotEmpty(message = "路由的资源不能为空")
    @ApiModelProperty("路由的资源，如：serviceId, instanceId, service version")
    private String resource;
    @NotNull(message = "策略id不能为空")
    @ApiModelProperty("策略id")
    private Long policyId;
}
