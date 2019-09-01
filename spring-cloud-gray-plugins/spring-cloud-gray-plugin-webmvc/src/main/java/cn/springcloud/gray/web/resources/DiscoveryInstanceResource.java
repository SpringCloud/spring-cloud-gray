package cn.springcloud.gray.web.resources;

import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gray/discovery/instance")
public class DiscoveryInstanceResource {

    @Autowired
    private InstanceDiscoveryClient instanceDiscoveryClient;


    /**
     * 设置本实例的实例状态
     *
     * @param status 实例状态
     */
    @RequestMapping(value = "/setStatus", method = RequestMethod.PUT)
    public void setStatus(@RequestParam("status") InstanceStatus status) {
        instanceDiscoveryClient.setStatus(status);
    }

}
