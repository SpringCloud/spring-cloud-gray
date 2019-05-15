package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayService;
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
    public List<GrayService> list() {
        return grayServerModule.listAllGrayServices();
    }


    @GetMapping(value = "/page")
    public ResponseEntity<List<GrayService>> list(
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayService> page = grayServerModule.listAllGrayServices(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayService>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GrayService info(@PathVariable("id") String id) {
        return grayServerModule.getGrayService(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        grayServerModule.deleteGrayService(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody GrayService grayPolicy) {
        grayServerModule.saveGrayService(grayPolicy);
    }
}
