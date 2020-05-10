package cn.springcloud.gray.server.module.route.policy.domain.query;

import cn.springcloud.gray.server.module.domain.DelFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RoutePolicyQuery {
    @ApiModelProperty("命名空间")
    private String ns;
    @ApiModelProperty("类型,如service_route, instance_route, service_multi_ver_route")
    private String type;
    @ApiModelProperty("模块id，用于搜索索引，如果serviceId")
    private String moduleId;
    @ApiModelProperty("路由的资源，如：serviceId, instanceId, service version")
    private String resource;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("是否删除")
    private DelFlag delFlag;
}
