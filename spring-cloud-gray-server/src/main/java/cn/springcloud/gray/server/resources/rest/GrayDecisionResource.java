package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.dao.mapper.GrayDecisionMapper;
import cn.springcloud.gray.server.dao.mapper.GrayPolicyMapper;
import cn.springcloud.gray.server.module.GrayModule;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/graydecision")
public class GrayDecisionResource {


    @Autowired
    private GrayDecisionService grayDecisionService;
    @Autowired
    private GrayDecisionMapper grayDecisionMapper;
    @Autowired
    private GrayModule grayModule;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<GrayDecision> all() {
        return grayDecisionService.findAllModel();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<GrayDecision> list(@RequestParam("policyId") Long policyId) {
        return grayDecisionService.findByPolicyId(policyId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        grayModule.deleteGrayDecision(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void update(GrayDecision grayDecision) {
        grayModule.updateGrayDecision(grayDecision);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void add(GrayDecision grayDecision) {
        grayModule.addGrayDecision(grayDecision);
    }

}
