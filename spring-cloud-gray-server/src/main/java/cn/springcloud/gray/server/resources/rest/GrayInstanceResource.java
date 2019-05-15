package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayInstance;
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
@RequestMapping("/gray/instance")
public class GrayInstanceResource {


    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"serviceId"})
    public List<GrayInstance> listByServiceId(@RequestParam("serviceId") String serviceId) {
        return grayServerModule.listGrayInstancesByServiceId(serviceId);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<List<GrayInstance>> page(
            @RequestParam("serviceId") String serviceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayInstance> page = grayServerModule.listGrayInstancesByServiceId(serviceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayInstance>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GrayInstance info(@PathVariable("id") String id) {
        return grayServerModule.getGrayInstance(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        grayServerModule.deleteGrayInstance(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody GrayInstance grayInstance) {
        grayServerModule.saveGrayInstance(grayInstance);
    }


    @RequestMapping(value = "{id}/switchStatus", method = RequestMethod.PUT)
    public void switchGrayStatus(@PathVariable("id") String instanceId,
                                 @ApiParam(value = "灰度开关{0: close, 1: open}", defaultValue = "0") @RequestParam("switch") int onoff) {
        switch (onoff) {
            case 1:
                grayServerModule.openGray(instanceId);
                return;
            case 0:
                grayServerModule.closeGray(instanceId);
                return;
            default:
                throw new UnsupportedOperationException("不支持的开关类型");
        }


    }


}
