package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayService;
import cn.springcloud.gray.server.resources.domain.ApiRes;
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
@RequestMapping("/gray/service")
public class GrayServiceResource {


    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiRes<List<GrayService>> list() {
        return ApiRes.<List<GrayService>>builder().code("0").data(grayServerModule.listAllGrayServices()).build();
    }


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayService>>> list(
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayService> page = grayServerModule.listAllGrayServices(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        ApiRes<List<GrayService>> data = ApiRes.<List<GrayService>>builder().code("0").data(page.getContent()).build();
        return new ResponseEntity<>(
                data,
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiRes<GrayService> info(@PathVariable("id") String id) {
        return ApiRes.<GrayService>builder().code("0").data(grayServerModule.getGrayService(id)).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") String id) {
        grayServerModule.deleteGrayService(id);
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<Void> save(@RequestBody GrayService grayPolicy) {
        grayServerModule.saveGrayService(grayPolicy);
        return ApiRes.<Void>builder().code("0").build();
    }
}
