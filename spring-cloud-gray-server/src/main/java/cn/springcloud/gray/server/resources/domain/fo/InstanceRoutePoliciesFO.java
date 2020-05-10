package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InstanceRoutePoliciesFO {

    @NotEmpty(message = "类型不能为空")
    @ApiModelProperty(value = "类型,如SERVICE_ROUTE, INSTANCE_ROUTE, SERVICE_MULTI_VER_ROUTE", example = "INSTANCE_ROUTE")
    private String type;
    /**
     * 模块id，用于搜索索引，如果serviceId
     */
    @NotEmpty(message = "模块Id不能为空")
    private String moduleId;
    /**
     * 路由的资源，如：serviceId, instanceId, service version
     */
    @NotEmpty(message = "路由的资源不能为空")
    private String[] resources;

    @NotEmpty(message = "策略id不能为空")
    @ApiModelProperty("策略id")
    private Long[] policyIds;
}
