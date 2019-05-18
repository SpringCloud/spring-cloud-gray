package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.server.module.GrayServerModule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用于接收注册中心实例变更信息
 */
@RestController
@RequestMapping("/gray/discover")
public class DiscoverInstanceResource {

    @Autowired
    private GrayServerModule grayServerModule;


    /**
     * 接收注册中心实例信息
     *
     * @param instanceInfo 实例信息
     * @return http status
     */
    @RequestMapping(value = "/instanceInfo", method = RequestMethod.POST)
    public ResponseEntity<Void> instanceInfo(@RequestBody InstanceInfo instanceInfo) {
        if (StringUtils.isNotEmpty(instanceInfo.getInstanceId()) && instanceInfo.getInstanceStatus() != null) {
            grayServerModule.updateInstanceStatus(instanceInfo.getInstanceId(), instanceInfo.getInstanceStatus());
        }
        return ResponseEntity.ok().build();
    }


}
