package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.GrayInstanceAlias;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.GrayServiceIdFinder;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.fo.GrayInstanceAliasFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
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

import java.util.Date;
import java.util.List;

import static cn.springcloud.gray.api.ApiRes.CODE_SUCCESS;

@RestController
@RequestMapping("/gray/instance")
public class GrayInstanceResource {


    @Autowired
    private GrayServerModule grayServerModule;
    @Autowired
    private UserModule userModule;
    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private GrayServiceIdFinder grayServiceIdFinder;


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayInstance>>> page(
            @RequestParam("serviceId") String serviceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayInstance> page = grayServerModule.listGrayInstancesByServiceId(serviceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        ApiRes<List<GrayInstance>> res = ApiRes.<List<GrayInstance>>builder()
                .code(CODE_SUCCESS)
                .data(page.getContent())
                .build();
        return new ResponseEntity<>(
                res,
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiRes<GrayInstance> info(@RequestParam("id") String id) {
        return ApiRes.<GrayInstance>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.getGrayInstance(id))
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@RequestParam("id") String id) {
        if (!serviceManageModule.hasServiceAuthority(
                grayServiceIdFinder.getServiceId(GrayModelType.INSTANCE, id))) {
            return ApiResHelper.notAuthority();
        }
        grayServerModule.deleteGrayInstance(id);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<Void> save(@RequestBody GrayInstance grayInstance) {
        if (StringUtils.isNotEmpty(userModule.getCurrentUserId())) {
            if (!serviceManageModule.hasServiceAuthority(grayInstance.getServiceId())) {
                return ApiResHelper.notAuthority();
            }
        }
        grayInstance.setOperator(userModule.getCurrentUserId());
        grayInstance.setOperateTime(new Date());
        grayServerModule.saveGrayInstance(grayInstance);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }


    @RequestMapping(value = "/switchStatus", method = RequestMethod.PUT)
    public ApiRes<Void> switchGrayStatus(@RequestParam("id") String instanceId,
                                         @ApiParam(value = "灰度开关{0: close, 1: open}", defaultValue = "0") @RequestParam("switch") int onoff) {

        if (StringUtils.isNotEmpty(userModule.getCurrentUserId())) {
            if (!serviceManageModule.hasServiceAuthority(
                    grayServiceIdFinder.getServiceId(GrayModelType.INSTANCE, instanceId))) {
                return ApiResHelper.notAuthority();
            }
        }

        switch (onoff) {
            case 1:
                grayServerModule.openGray(instanceId);
                return ApiRes.<Void>builder()
                        .code(CODE_SUCCESS)
                        .build();
            case 0:
                grayServerModule.closeGray(instanceId);
                return ApiRes.<Void>builder()
                        .code(CODE_SUCCESS)
                        .build();
            default:
                throw new UnsupportedOperationException("不支持的开关类型");
        }
    }

    @RequestMapping(value = "/switchLock", method = RequestMethod.PUT)
    public ApiRes<Void> switchGrayLock(@RequestParam("id") String instanceId,
                                       @ApiParam(value = "开关{0: unlock, 1: lock}", defaultValue = "0") @RequestParam("switch") int onoff) {

        if (StringUtils.isNotEmpty(userModule.getCurrentUserId())) {
            if (!serviceManageModule.hasServiceAuthority(
                    grayServiceIdFinder.getServiceId(GrayModelType.INSTANCE, instanceId))) {
                return ApiResHelper.notAuthority();
            }
        }

        switch (onoff) {
            case 1:
                grayServerModule.openGrayLock(instanceId);
                return ApiRes.<Void>builder()
                        .code(CODE_SUCCESS)
                        .build();
            case 0:
                grayServerModule.closeGrayLock(instanceId);
                return ApiRes.<Void>builder()
                        .code(CODE_SUCCESS)
                        .build();
            default:
                throw new UnsupportedOperationException("不支持的开关类型");
        }
    }


    @RequestMapping(value = "/updateAliases", method = RequestMethod.PUT)
    public ApiRes<Void> updateAliases(@Validated @RequestBody GrayInstanceAliasFO fo) {
        String serviceId = grayServiceIdFinder.getServiceId(GrayModelType.INSTANCE, fo.getInstanceId());
        if (!serviceManageModule.hasServiceAuthority(serviceId)) {
            return ApiResHelper.notAuthority();
        }
        GrayInstanceAlias grayInstanceAlias = GrayInstanceAlias.builder()
                .instanceId(fo.getInstanceId())
                .aliases(fo.getAliases())
                .serviceId(serviceId)
                .build();
        grayServerModule.updateInstanceAliases(grayInstanceAlias, userModule.getCurrentUserId());

        return ApiResHelper.success();
    }


}
