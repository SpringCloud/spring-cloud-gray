package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.gray.domain.query.GrayServiceQuery;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import cn.springcloud.gray.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api
@RestController
@RequestMapping("/gray/service")
public class GrayServiceResource {


    @Autowired
    private GrayServerModule grayServerModule;
    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private UserModule userModule;
    @Autowired
    private AuthorityModule authorityModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiRes<List<GrayService>> list() {
        return ApiRes.<List<GrayService>>builder().code("0").data(grayServerModule.listAllGrayServices()).build();
    }


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayService>>> list(
            @RequestParam(value = "namespace", required = false) String namespace,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        if (StringUtils.isEmpty(namespace)) {
            return ResponseEntity.ok(ApiResHelper.failed("namespace 不能为空"));
        }
        if (!authorityModule.hasNamespaceAuthority(namespace)) {
            return ResponseEntity.ok(ApiResHelper.notAuthority());
        }
        GrayServiceQuery serviceQuery = GrayServiceQuery.builder()
                .namespace(namespace)
                .userId(userModule.getCurrentUserId())
                .build();
        Page<GrayService> page = grayServerModule.queryGrayServices(serviceQuery, pageable);
        return PaginationUtils.generatePaginationResponseResult(page);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiRes<GrayService> info(@PathVariable("id") String id) {
        return ApiRes.<GrayService>builder().code("0").data(grayServerModule.getGrayService(id)).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") String id) {
        if (!serviceManageModule.hasServiceAuthority(id)
                || !serviceManageModule.isServiceOwner(id, userModule.getCurrentUserId())) {
            return ApiResHelper.notAuthority();
        }

        grayServerModule.deleteGrayService(id);
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<Void> save(@RequestBody GrayService grayService) {
        if (grayServerModule.getGrayService(grayService.getServiceId()) != null) {
            if (!serviceManageModule.hasServiceAuthority(grayService.getServiceId())) {
                return ApiResHelper.notAuthority();
            }
        }
        grayService.setOperator(userModule.getCurrentUserId());
        grayService.setOperateTime(new Date());
        grayServerModule.saveGrayService(grayService);
        return ApiRes.<Void>builder().code("0").build();
    }
}
