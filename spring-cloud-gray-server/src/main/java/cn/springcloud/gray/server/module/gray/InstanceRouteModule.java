package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicies;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springcloud.gray.server.module.gray.domain.query.InstanceRoutePolicyQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-22 07:31
 */
public interface InstanceRouteModule {


    Page<InstanceRoutePolicy> queryInstanceRoutePolicies(InstanceRoutePolicyQuery query, Pageable pageable);

    InstanceRoutePolicy saveInstanceRoutePolicy(InstanceRoutePolicy instanceRoutePolicy);

    void updateInstanceRoutePolicyDelFlag(String id, boolean newDelFlag, String operator);

    int saveInstanceRoutePolicies(InstanceRoutePolicies instanceRoutePolicies);

    InstanceRoutePolicy getInstanceRoutePolicy(String id);

    List<InstanceRoutePolicy> findAllRoutePoliciesByInstanceId(String instanceId, Boolean delFlag);
}
