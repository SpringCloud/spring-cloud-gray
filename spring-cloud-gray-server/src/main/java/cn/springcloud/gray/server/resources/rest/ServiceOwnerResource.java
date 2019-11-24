package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import cn.springcloud.gray.server.module.user.domain.ServiceOwnerQuery;
import cn.springcloud.gray.server.resources.domain.fo.ServiceOwnerFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;

@Api
@RestController
@RequestMapping("/gray/service/owner")
public class ServiceOwnerResource {

    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private UserModule userModule;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务id", dataType = "string"),
            @ApiImplicitParam(name = "queryItem", value = "查询项,{0:全部, 1:已绑定owner, 2:未绑定owner}", dataType = "int",
                    defaultValue = "0", allowableValues = "0,1,2")})

    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<ServiceOwner>>> list(
            @RequestParam(value = "serviceId", required = false) String serviceId,
            @RequestParam(value = "queryItem", required = false) int queryItem,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        ServiceOwnerQuery query = ServiceOwnerQuery.builder()
                .serviceId(serviceId)
                .queryItem(queryItem)
                .build();
        Page<ServiceOwner> serviceOwnerPage = serviceManageModule.queryServiceOwners(query, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(serviceOwnerPage);
        ApiRes<List<ServiceOwner>> data = ApiRes.<List<ServiceOwner>>builder()
                .code(ApiRes.CODE_SUCCESS)
                .data(serviceOwnerPage.getContent())
                .build();
        return new ResponseEntity<>(
                data,
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiRes<Void> save(@Validated @RequestBody ServiceOwnerFO serviceOwnerFO) {
        ServiceOwner serviceOwner = serviceManageModule.getServiceOwner(serviceOwnerFO.getServiceId());
        if (serviceOwner == null) {
            return ApiResHelper.notFound();
        }
        if (StringUtils.isNotEmpty(serviceOwner.getUserId()) &&
                !StringUtils.equals(serviceOwner.getUserId(), userModule.getCurrentUserId())) {
            return ApiResHelper.notAuthority();
        }
        if (userModule.getUserInfo(serviceOwnerFO.getUserId()) == null) {
            return ApiResHelper.notFound("has not found user");
        }
        serviceManageModule.transferServiceOwner(serviceOwnerFO.getServiceId(), serviceOwnerFO.getUserId());
        return ApiRes.<Void>builder().code("0").build();
    }


}
