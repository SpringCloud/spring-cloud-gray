package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.gray.GrayServiceIdFinder;
import cn.springcloud.gray.server.module.gray.InstanceRouteModule;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicies;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springcloud.gray.server.module.gray.domain.query.InstanceRoutePolicyQuery;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.fo.InstanceRoutePoliciesFO;
import cn.springcloud.gray.server.resources.domain.fo.InstanceRoutePolicyFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 07:27
 */
@RestController
@RequestMapping("/gray/instance/route/policy")
public class InstanceRoutePolicyResource {
    @Autowired
    private InstanceRouteModule instanceRouteModule;
    @Autowired
    private UserModule userModule;
    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private GrayServiceIdFinder grayServiceIdFinder;


    @GetMapping(value = "/query")
    public ResponseEntity<ApiRes<List<InstanceRoutePolicy>>> query(
            InstanceRoutePolicyQuery query,
            @ApiParam @PageableDefault(sort = "operateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<InstanceRoutePolicy> page = instanceRouteModule.queryInstanceRoutePolicies(query, pageable);
        return PaginationUtils.generatePaginationResponseResult(page);
    }

    @PostMapping(value = "/addNews")
    public ApiRes<Integer> addNews(@Validated @RequestBody InstanceRoutePoliciesFO fo) {
        if (!hasAllServiceAuthorityByInstanceIds(fo.getInstanceIds())) {
            return ApiResHelper.notAuthority();
        }
        InstanceRoutePolicies instanceRoutePolicies =
                InstanceRoutePolicies.builder()
                        .instanceIds(fo.getInstanceIds())
                        .policyIds(fo.getPolicyIds())
                        .operator(userModule.getCurrentUserId())
                        .operateTime(new Date())
                        .build();
        int count = instanceRouteModule.saveInstanceRoutePolicies(instanceRoutePolicies);
        return ApiResHelper.successData(count);
    }

    @PostMapping(value = "/save")
    public ApiRes<InstanceRoutePolicy> save(@Validated @RequestBody InstanceRoutePolicyFO fo) {
        if (!hasServiceAuthorityByInstanceId(fo.getInstanceId())) {
            return ApiResHelper.notAuthority();
        }
        InstanceRoutePolicy instanceRoutePolicy =
                InstanceRoutePolicy.builder()
                        .instanceId(fo.getInstanceId())
                        .policyId(fo.getPolicyId())
                        .operator(userModule.getCurrentUserId())
                        .operateTime(new Date())
                        .build();
        instanceRoutePolicy = instanceRouteModule.saveInstanceRoutePolicy(instanceRoutePolicy);
        return ApiResHelper.successData(instanceRoutePolicy);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(
            @ApiParam(name = "id", required = true) @RequestParam("id") String id) {
        InstanceRoutePolicy instanceRoutePolicy = instanceRouteModule.getInstanceRoutePolicy(id);
        if (Objects.isNull(instanceRoutePolicy)) {
            return ApiResHelper.notFound();
        }
        if (!hasServiceAuthorityByInstanceId(instanceRoutePolicy.getInstanceId())) {
            return ApiResHelper.notAuthority();
        }
        instanceRouteModule.updateInstanceRoutePolicyDelFlag(id, true, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(
            @ApiParam(name = "id", required = true) @RequestParam("id") String id) {
        InstanceRoutePolicy instanceRoutePolicy = instanceRouteModule.getInstanceRoutePolicy(id);
        if (Objects.isNull(instanceRoutePolicy)) {
            return ApiResHelper.notFound();
        }
        if (!hasServiceAuthorityByInstanceId(instanceRoutePolicy.getInstanceId())) {
            return ApiResHelper.notAuthority();
        }
        instanceRouteModule.updateInstanceRoutePolicyDelFlag(id, false, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }


    private boolean hasAllServiceAuthorityByInstanceIds(String[] instanceIds) {
        for (String instanceId : instanceIds) {
            if (!hasServiceAuthorityByInstanceId(instanceId)) {
                return false;
            }
        }
        return true;
    }


    private boolean hasServiceAuthorityByInstanceId(String instanceId) {
        return serviceManageModule.hasServiceAuthority(
                grayServiceIdFinder.getServiceId(GrayModelType.INSTANCE, instanceId));
    }
}
