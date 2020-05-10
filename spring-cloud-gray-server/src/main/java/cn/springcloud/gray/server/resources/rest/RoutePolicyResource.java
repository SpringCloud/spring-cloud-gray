package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.RoutePolicy;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.server.module.route.policy.domain.RouteResourcePolicies;
import cn.springcloud.gray.server.module.route.policy.domain.query.RoutePolicyQuery;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.fo.InstanceRoutePoliciesFO;
import cn.springcloud.gray.server.resources.domain.fo.InstanceRoutePolicyFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import cn.springcloud.gray.server.utils.SessionUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 07:27
 */
@RestController
@RequestMapping("/route/policy")
public class RoutePolicyResource {
    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private RoutePolicyModule routePolicyModule;
    @Autowired
    private UserModule userModule;


    @GetMapping(value = "/query")
    public ResponseEntity<ApiRes<List<RoutePolicyRecord>>> query(
            RoutePolicyQuery query,
            @ApiParam @PageableDefault(sort = "operateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RoutePolicyRecord> page = routePolicyModule.queryRoutePolicies(query, pageable);
        return PaginationUtils.generatePaginationResponseResult(page);
    }

    @PostMapping(value = "/addNews")
    public ApiRes<Integer> addNews(@Validated @RequestBody InstanceRoutePoliciesFO fo) {
        RouteResourcePolicies routeResourcePolicies =
                RouteResourcePolicies.builder()
                        .type(fo.getType())
                        .moduleId(fo.getModuleId())
                        .resources(fo.getResources())
                        .policyIds(fo.getPolicyIds())
                        .build();

        int count = routePolicyModule.saveRoutePolicies(
                routeResourcePolicies, userModule.getCurrentUserId(), true);

        return ApiResHelper.successData(count);
    }

    @PostMapping(value = "/save")
    public ApiRes<RoutePolicyRecord> save(@Validated @RequestBody InstanceRoutePolicyFO fo) {
        RoutePolicy routePolicy = RoutePolicy.builder()
                .type(fo.getType())
                .moduleId(fo.getModuleId())
                .resource(fo.getResource())
                .policyId(fo.getPolicyId())
                .build();

        String curUserId = userModule.getCurrentUserId();
        if (!routePolicyModule.hasResourceAuthority(SessionUtils.currentNamespace(), curUserId, routePolicy)) {
            return ApiResHelper.notAuthority();
        }
        RoutePolicyRecord routePolicyRecord =
                routePolicyModule.addRoutePolicy(SessionUtils.currentNamespace(), routePolicy, curUserId);
        return ApiResHelper.successData(routePolicyRecord);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        return updateRoutePolicyRecordState(id, true);
    }

    @RequestMapping(value = "/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        return updateRoutePolicyRecordState(id, false);
    }

    /**
     * 更新状态
     *
     * @param id
     * @param delFlag
     * @return
     */
    private ApiRes<Void> updateRoutePolicyRecordState(Long id, boolean delFlag) {
        RoutePolicyRecord routePolicyRecord = routePolicyModule.getRoutePolicy(id);
        if (Objects.isNull(routePolicyRecord)) {
            return ApiResHelper.notFound();
        }

        if (!authorityModule.hasNamespaceAuthority(routePolicyRecord.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        routePolicyModule.updateRoutePolicyDelFlag(id, delFlag, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }


}
