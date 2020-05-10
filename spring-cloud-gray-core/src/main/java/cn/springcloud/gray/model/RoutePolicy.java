package cn.springcloud.gray.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-10 17:14
 */
@Data
@Builder
@AllArgsConstructor
public class RoutePolicy {

    private final Long id;
    private final String type;
    /**
     * 模块id，用于搜索索引，如果serviceId
     */
    private final String moduleId;
    /**
     * 路由的资源，如：serviceId, instanceId, service version
     */
    private final String resource;

    private final Long policyId;

}
