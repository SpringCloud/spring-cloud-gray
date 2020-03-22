package cn.springcloud.gray.server.resources.rest.remote;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.server.constant.Version;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import cn.springcloud.gray.server.utils.ApiResHelper;
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
@RequestMapping("/gray/v2")
public class GrayResourceV2 {


    @Autowired
    private GrayModule grayModule;

    @ApiOperation("返回所有已经打开灰度状态的实例信息（包含决策信息）")
    @RequestMapping(value = "/instances/enable", method = RequestMethod.GET)
    public ApiRes<List<GrayInstance>> allOpens() {
        return ApiResHelper.successData(grayModule.allOpenInstances(Version.V2));
    }


    @ApiOperation("返回指定实例的信息（包含决策信息）")
    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public ApiRes<GrayInstance> instance(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId) {
        return ApiResHelper.successData(grayModule.getGrayInstance(serviceId, instanceId, Version.V2));
    }

    @ApiOperation("返回指定实例的灰度追踪信息（包含决策信息）")
    @RequestMapping(value = "/trackDefinitions", method = RequestMethod.GET)
    public ApiRes<List<GrayTrackDefinition>> trackDefinitions(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId) {
        return ApiResHelper.successData(grayModule.getTrackDefinitions(serviceId, instanceId));
    }


    @ApiOperation("返回所有的灰度策略")
    @RequestMapping(value = "/policyDefinitions", method = RequestMethod.GET)
    public ApiRes<List<GrayPolicyDecision>> policyDefinitions() {
        return ApiResHelper.successData(grayModule.allGrayPolicies());
    }

    @ApiOperation("返回最大的sortmark")
    @RequestMapping(value = "/maxSortMark", method = RequestMethod.GET)
    public ApiRes<Long> getMaxSortMark() {
        return ApiResHelper.successData(grayModule.getMaxSortMark());
    }
}
