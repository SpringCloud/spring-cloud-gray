package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
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

@RestController
@RequestMapping("/gray/policy")
public class GrayPolicyResource {

    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "list", method = RequestMethod.GET, params = "instanceId")
    public List<GrayPolicy> listByInstanceId(@RequestParam("instanceId") String instanceId) {
        return grayServerModule.listGrayPoliciesByInstanceId(instanceId);
    }


    @GetMapping(value = "/page")
    public ResponseEntity<List<GrayPolicy>> page(
            @RequestParam("instanceId") String instanceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayPolicy> page = grayServerModule.listGrayPoliciesByInstanceId(instanceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayPolicy>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        grayServerModule.deleteGrayPolicy(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody GrayPolicy grayPolicy) {
        grayServerModule.saveGrayPolicy(grayPolicy);
    }
}
