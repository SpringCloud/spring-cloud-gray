package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.resources.domain.ApiRes;
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

import java.util.List;

import static cn.springcloud.gray.server.resources.domain.ApiRes.CODE_SUCCESS;

@RestController
@RequestMapping("/gray/instance")
public class GrayInstanceResource {


    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"serviceId"})
    public ApiRes<List<GrayInstance>> listByServiceId(@RequestParam("serviceId") String serviceId) {
        return ApiRes.<List<GrayInstance>>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.listGrayInstancesByServiceId(serviceId))
                .build();
    }

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
        grayServerModule.deleteGrayInstance(id);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<Void> save(@RequestBody GrayInstance grayInstance) {
        grayServerModule.saveGrayInstance(grayInstance);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }


    @RequestMapping(value = "/switchStatus", method = RequestMethod.PUT)
    public ApiRes<Void> switchGrayStatus(@RequestParam("id") String instanceId,
                                         @ApiParam(value = "灰度开关{0: close, 1: open}", defaultValue = "0") @RequestParam("switch") int onoff) {
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


}
