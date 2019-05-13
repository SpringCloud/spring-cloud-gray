package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.dao.mapper.GrayInstanceMapper;
import cn.springcloud.gray.server.module.GrayModule;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.service.GrayInstanceService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grayinstance")
public class GrayInstanceResource {


    @Autowired
    private GrayInstanceService grayInstanceService;
    @Autowired
    private GrayInstanceMapper grayInstanceMapper;
    @Autowired
    private GrayModule grayModule;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<GrayInstance> all() {
        return grayInstanceService.findAllModel();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<GrayInstance> list(@RequestParam("serviceId") String serviceId) {
        return grayInstanceService.findByServiceId(serviceId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        grayModule.deleteGrayInstance(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void update(GrayInstance grayInstance) {
        grayModule.updateGrayInstance(grayInstance);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void add(GrayInstance grayInstance) {
        grayModule.addGrayInstance(grayInstance);
    }


    @RequestMapping(value = "{id}/switchStatus", method = RequestMethod.PUT)
    public void switchGrayStatus(@PathVariable("id") String instanceId,
                                 @ApiParam(value = "灰度开关{0: close, 1: open}", defaultValue = "0") @RequestParam("switch") int onoff) {
        switch (onoff) {
            case 1:
                grayModule.openGray(instanceId);
                return;
            case 0:
                grayModule.closeGray(instanceId);
                return;
            default:
                throw new UnsupportedOperationException("不支持的开关类型");
        }


    }


}
