package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.dao.mapper.GrayPolicyMapper;
import cn.springcloud.gray.server.module.GrayModule;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.resources.domain.Res;
import cn.springcloud.gray.server.service.GrayPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/graypolicy")
public class GrayPolicyResource {

    @Autowired
    private GrayPolicyService grayPolicyService;
    @Autowired
    private GrayModule grayModule;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<GrayPolicy> all() {
        return grayPolicyService.findAllModel();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<GrayPolicy> list(@RequestParam("instanceId") String instanceId) {
        return grayPolicyService.findByInstanceId(instanceId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        grayModule.deleteGrayPolicy(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void update(GrayPolicy grayPolicy) {
        grayModule.updateGrayPolicy(grayPolicy);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void add(GrayPolicy grayPolicy) {
        grayModule.addGrayPolicy(grayPolicy);
    }
}
