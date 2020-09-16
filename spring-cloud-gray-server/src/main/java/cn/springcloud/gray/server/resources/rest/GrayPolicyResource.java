package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.gray.GrayPolicyModule;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import cn.springcloud.gray.server.module.gray.domain.query.GrayPolicyQuery;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import cn.springcloud.gray.utils.StringUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static cn.springcloud.gray.api.ApiRes.CODE_SUCCESS;

@RestController
@RequestMapping("/gray/policy")
public class GrayPolicyResource {

    @Autowired
    private GrayPolicyModule grayPolicyModule;
    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private NamespaceFinder namespaceFinder;
    @Autowired
    private UserModule userModule;

    @RequestMapping(value = "list", method = RequestMethod.GET, params = "namespace")
    public ApiRes<List<GrayPolicy>> listByInstanceId(
            @Validated @RequestParam(value = "namespace", required = false) String namespace) {
        return ApiRes.<List<GrayPolicy>>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.listEnabledGrayPoliciesByNamespace(namespace))
                .build();
    }


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayPolicy>>> page(
            @Validated GrayPolicyQuery query,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (StringUtils.isEmpty(query.getNamespace())) {
            return ResponseEntity.ok(ApiResHelper.failed("namespace 不能为空"));
        }
        if (!authorityModule.hasNamespaceAuthority(query.getNamespace())) {
            return ResponseEntity.ok(ApiResHelper.notAuthority());
        }
        Page<GrayPolicy> page = grayPolicyModule.queryGrayPolicies(query, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        ApiRes<List<GrayPolicy>> res = ApiRes.<List<GrayPolicy>>builder()
                .code(CODE_SUCCESS)
                .data(page.getContent())
                .build();
        return new ResponseEntity<ApiRes<List<GrayPolicy>>>(
                res,
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.POLICY, id))) {
            return ApiResHelper.notAuthority();
        }
        grayPolicyModule.deleteGrayPolicy(id, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/{id}/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(@PathVariable("id") Long id) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.POLICY, id))) {
            return ApiResHelper.notAuthority();
        }
        grayPolicyModule.recoverGrayPolicy(id, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayPolicy> save(@RequestBody GrayPolicy grayPolicy) {
        if (!authorityModule.hasNamespaceAuthority(grayPolicy.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        grayPolicy.setOperator(userModule.getCurrentUserId());
        grayPolicy.setOperateTime(new Date());
        return ApiRes.<GrayPolicy>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.saveGrayPolicy(grayPolicy))
                .build();
    }

    @RequestMapping(value = "/newPolicy", method = RequestMethod.POST)
    public ApiRes<GrayPolicyDecision> newPolicy(@RequestBody GrayPolicyDecision grayPolicyDecision) {
        GrayPolicy grayPolicy = grayPolicyDecision.getGrayPolicy();
        if (!authorityModule.hasNamespaceAuthority(grayPolicy.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        grayPolicy.setOperator(userModule.getCurrentUserId());
        grayPolicy.setOperateTime(new Date());
        grayPolicyDecision.getGrayDecisions().forEach(decision -> {
            decision.setOperator(grayPolicy.getOperator());
            decision.setOperateTime(grayPolicy.getOperateTime());
        });

        return ApiRes.<GrayPolicyDecision>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.newGrayPolicy(grayPolicyDecision))
                .build();
    }
}
