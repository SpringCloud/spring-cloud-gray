package cn.springcloud.gray.web.resources;

import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    @PutMapping(value = "/setStatus")
    public Mono<Void> setStatus(@RequestParam("status") InstanceStatus status) {
        instanceDiscoveryClient.setStatus(status);
        return Mono.empty();
    }

}
