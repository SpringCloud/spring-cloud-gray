package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
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
@RequestMapping("/gray/policy")
public class GrayPolicyResource {

    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "list", method = RequestMethod.GET, params = "instanceId")
    public ApiRes<List<GrayPolicy>> listByInstanceId(@RequestParam("instanceId") String instanceId) {
        return ApiRes.<List<GrayPolicy>>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.listGrayPoliciesByInstanceId(instanceId))
                .build();
    }


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayPolicy>>> page(
            @RequestParam("instanceId") String instanceId,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayPolicy> page = grayServerModule.listGrayPoliciesByInstanceId(instanceId, pageable);
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
        grayServerModule.deleteGrayPolicy(id);
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayPolicy> save(@RequestBody GrayPolicy grayPolicy) {
        return ApiRes.<GrayPolicy>builder()
                .code(CODE_SUCCESS)
                .data(grayServerModule.saveGrayPolicy(grayPolicy))
                .build();
    }
}
