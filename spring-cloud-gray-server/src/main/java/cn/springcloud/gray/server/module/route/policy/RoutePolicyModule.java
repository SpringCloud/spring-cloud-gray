package cn.springcloud.gray.server.module.route.policy;

import cn.springcloud.gray.function.Predicate3;
import cn.springcloud.gray.model.RoutePolicy;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.server.module.route.policy.domain.RouteResourcePolicies;
import cn.springcloud.gray.server.module.route.policy.domain.query.RoutePolicyQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-22 07:31
 */
public interface RoutePolicyModule {


    Page<RoutePolicyRecord> queryRoutePolicies(RoutePolicyQuery query, Pageable pageable);


    List<RoutePolicyRecord> findAllRoutePolicies(RoutePolicyQuery query);

    /**
     * @param routePolicy
     * @param operatorId  操作人id
     * @return
     */
    RoutePolicyRecord addRoutePolicy(String ns, RoutePolicy routePolicy, String operatorId);


    void physicsDeleteRoutePolicy(String type, String moduleId, String resource);

    /**
     * 更新状态
     *
     * @param id
     * @param newDelFlag
     * @param operatorId
     */
    void updateRoutePolicyDelFlag(Long id, boolean newDelFlag, String operatorId);

    int saveRoutePolicies(RouteResourcePolicies routeResourcePolicies, String operatorId, boolean needAuthority);

    RoutePolicyRecord getRoutePolicy(Long id);

    boolean hasResourceAuthority(String userId, RoutePolicyRecord routePolicyRecord);

    boolean hasResourceAuthority(String ns, String userId, RoutePolicy routePolicy);


    void registerResourceAuthorityPredicate(String type, Predicate3<String, String, RoutePolicy> predicate);

    Predicate3<String, String, RoutePolicy> getResourceAuthorityPredicate(String type);


}
