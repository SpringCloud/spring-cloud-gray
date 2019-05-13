package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.dao.mapper.GrayServiceMapper;
import cn.springcloud.gray.server.module.GrayModule;
import cn.springcloud.gray.server.module.domain.GrayService;
import cn.springcloud.gray.server.service.GrayServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grayservice")
public class GrayServiceResource {

    @Autowired
    private GrayServiceService grayServiceService;

    @Autowired
    private GrayModule grayModule;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<GrayService> all() {
        return grayServiceService.findAllModel();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        grayModule.deleteGrayService(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void update(GrayService grayPolicy) {
        grayServiceService.saveModel(grayPolicy);
    }
}
