package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.gray.GrayModelType;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.GrayServiceIdFinder;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.ApiRes;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static cn.springcloud.gray.server.resources.domain.ApiRes.CODE_SUCCESS;

@RestController
@RequestMapping("/gray/decision")
public class GrayDecisionResource {


    @Autowired
    private GrayServerModule grayServerModule;
    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private GrayServiceIdFinder grayServiceIdFinder;
    @Autowired
    private UserModule userModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"policyId"})
    public ApiRes<List<GrayDecision>> list(@RequestParam("policyId") Long policyId) {
        return ApiRes.<List<GrayDecision>>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.listGrayDecisionsByPolicyId(policyId))
                .build();
    }

    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayDecision>>> page(
            @RequestParam("policyId") Long policyId,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayDecision> page = grayServerModule.listGrayDecisionsByPolicyId(policyId, pageable);
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
        return ApiRes.<GrayDecision>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.getGrayDecision(id))
                .build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        if (!serviceManageModule.hasServiceAuthority(
                grayServiceIdFinder.getServiceId(GrayModelType.DECISION, id))) {
            return ApiResHelper.notAuthority();
        }
        grayServerModule.deleteGrayDecision(id);
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayDecision> save(@RequestBody GrayDecision grayDecision) {
        if (!serviceManageModule.hasServiceAuthority(
                grayServiceIdFinder.getServiceId(GrayModelType.POLICY, grayDecision.getPolicyId()))) {
            return ApiResHelper.notAuthority();
        }
        grayDecision.setOperator(userModule.getCurrentUserId());
        grayDecision.setOperateTime(new Date());
        return ApiRes.<GrayDecision>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.saveGrayDecision(grayDecision))
                .build();
    }

}
