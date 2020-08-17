package cn.springcloud.gray.server.module.route.policy.domain;

import cn.springcloud.gray.model.RoutePolicyType;
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
public class RouteResourcePolicies {

    private String namespace;
    /**
     * 类型,如service_route, instance_route, service_multi_ver_route,
     * 参看 {@link RoutePolicyType}
     */
    private String type;
    /**
     * 模块id，用于搜索索引，如果serviceId
     */
    private String moduleId;
    /**
     * 路由的资源，如：serviceId, instanceId, service version
     */
    private String[] resources;
    /**
     * 策略ids
     */
    private Long[] policyIds;

}
