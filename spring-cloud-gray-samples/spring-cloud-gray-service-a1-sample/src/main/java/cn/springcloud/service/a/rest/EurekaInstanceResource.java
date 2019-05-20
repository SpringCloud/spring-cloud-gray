package cn.springcloud.service.a.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gray/instance")
public class EurekaInstanceResource {

    @Autowired
    private EurekaServiceRegistry eurekaServiceRegistry;
    @Autowired
    private EurekaRegistration eurekaRegistration;

    @RequestMapping(value = "setStatus", method = RequestMethod.PUT)
    public void setStatus(@RequestParam("status") String status) {
        eurekaServiceRegistry.setStatus(eurekaRegistration, status);
    }

}
