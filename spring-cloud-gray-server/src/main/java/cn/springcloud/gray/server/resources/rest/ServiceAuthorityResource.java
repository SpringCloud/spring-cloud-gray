package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import cn.springcloud.gray.server.resources.domain.fo.ServiceAuthorityFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.Api;
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

import java.util.List;

@Api
@RestController
@RequestMapping("/gray/service/authority")
public class ServiceAuthorityResource {

    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private UserModule userModule;


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<UserServiceAuthority>>> list(
            @RequestParam("serviceId") String serviceId,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserServiceAuthority> serviceAuthorityPage = serviceManageModule.listServiceAuthorities(serviceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(serviceAuthorityPage);
        ApiRes<List<UserServiceAuthority>> data = ApiRes.<List<UserServiceAuthority>>builder()
                .code(ApiRes.CODE_SUCCESS)
                .data(serviceAuthorityPage.getContent())
                .build();
        return new ResponseEntity<>(
                data,
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        UserServiceAuthority serviceAuthority = serviceManageModule.getServiceAuthority(id);
        if (serviceAuthority == null) {
            return ApiResHelper.notFound();
        }
        String serviceId = serviceAuthority.getServiceId();
        if (!serviceManageModule.hasServiceAuthority(serviceId)) {
            return ApiResHelper.notAuthority();
        }
        if (serviceManageModule.isServiceOwner(serviceId, serviceAuthority.getUserId())) {
            return ApiRes.<Void>builder().code("403").message("service owner is can not delete").build();
        }

        serviceManageModule.deleteServiceAuthority(id);
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<UserServiceAuthority> save(@RequestBody ServiceAuthorityFO serviceAuthorityFO) {
        if (!serviceManageModule.hasServiceAuthority(serviceAuthorityFO.getServiceId())) {
            return ApiResHelper.notAuthority();
        }
        if (userModule.getUserInfo(serviceAuthorityFO.getUserId()) == null) {
            return ApiResHelper.notFound("has not found user");
        }
        UserServiceAuthority serviceAuthority =
                serviceManageModule.addServiceAuthority(serviceAuthorityFO.getServiceId(), serviceAuthorityFO.getUserId());
        return ApiResHelper.successData(serviceAuthority);
    }

}
