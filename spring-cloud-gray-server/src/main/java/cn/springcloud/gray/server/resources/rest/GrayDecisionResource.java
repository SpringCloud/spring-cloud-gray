package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.gray.GrayPolicyModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.gray.domain.query.GrayDecisionQuery;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
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
@RequestMapping("/gray/decision")
public class GrayDecisionResource {


    @Autowired
    private GrayPolicyModule grayPolicyModule;
    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private NamespaceFinder namespaceFinder;
    @Autowired
    private UserModule userModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"policyId"})
    public ApiRes<List<GrayDecision>> list(@RequestParam("policyId") Long policyId) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.POLICY, policyId))) {
            return ApiResHelper.notAuthority();
        }
        return ApiRes.<List<GrayDecision>>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.listEnabledGrayDecisionsByPolicyId(policyId))
                .build();
    }

    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayDecision>>> page(
            @Validated GrayDecisionQuery query,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.POLICY, query.getPolicyId()))) {
            return ResponseEntity.ok(ApiResHelper.notAuthority());
        }
        Page<GrayDecision> page = grayPolicyModule.queryGrayDecisions(query, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiRes.<List<GrayDecision>>builder()
                        .code(CODE_SUCCESS)
                        .data(page.getContent())
                        .build(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiRes<GrayDecision> info(@PathVariable("id") Long id) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.DECISION, id))) {
            return ApiResHelper.notAuthority();
        }
        return ApiRes.<GrayDecision>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.getGrayDecision(id))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.DECISION, id))) {
            return ApiResHelper.notAuthority();
        }
        grayPolicyModule.deleteGrayDecision(id, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/{id}/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(@PathVariable("id") Long id) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.DECISION, id))) {
            return ApiResHelper.notAuthority();
        }
        grayPolicyModule.recoverGrayDecision(id, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayDecision> save(@RequestBody GrayDecision grayDecision) {
        if (!authorityModule.hasNamespaceAuthority(
                namespaceFinder.getNamespaceCode(GrayModelType.POLICY, grayDecision.getPolicyId()))) {
            return ApiResHelper.notAuthority();
        }
        grayDecision.setOperator(userModule.getCurrentUserId());
        grayDecision.setOperateTime(new Date());
        return ApiRes.<GrayDecision>builder()
                .code(CODE_SUCCESS)
                .data(grayPolicyModule.saveGrayDecision(grayDecision))
                .build();
    }

}
