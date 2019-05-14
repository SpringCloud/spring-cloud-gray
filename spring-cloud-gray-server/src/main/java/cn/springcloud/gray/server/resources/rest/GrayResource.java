package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.server.discovery.ServiceDiscover;
import cn.springcloud.gray.server.module.GrayModle;
import cn.springcloud.gray.server.module.GrayServerModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("gray-client调用的接口")
@RestController
@RequestMapping("/gray")
public class GrayResource {


    @Autowired
    private GrayModle grayModle;


    @ApiOperation("返回所有已经打开灰度状态的实例信息（包含决策信息）")
    @RequestMapping(value = "/instances/enable", method = RequestMethod.GET)
    public List<GrayInstance> allOpens() {
        return grayModle.allOpenInstances();
    }


    @ApiOperation("返回指定实例的信息（包含决策信息）")
    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public GrayInstance instance(@RequestParam("serviceId") String serviceId, @RequestParam("instanceId") String instanceId) {
        return grayModle.getGrayInstance(serviceId, instanceId);
    }

}
