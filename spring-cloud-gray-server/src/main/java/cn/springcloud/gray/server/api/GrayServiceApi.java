package cn.springcloud.gray.server.api;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.DecisionDefinition;
import cn.springcloud.gray.core.PolicyDefinition;
import cn.springcloud.gray.core.GrayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/gray")
public interface GrayServiceApi {


    @RequestMapping(value = "/services", method = RequestMethod.GET)
    List<GrayService> services();

    @RequestMapping(value = "/services/enable", method = RequestMethod.GET)
    List<GrayService> enableServices();


    @RequestMapping(value = "/services/{serviceId}", method = RequestMethod.GET)
    GrayService service(@PathVariable("serviceId") String serviceId);


    @RequestMapping(value = "/services/{serviceId}/instances", method = RequestMethod.GET)
    List<GrayInstance> instances(@PathVariable("serviceId") String serviceId);


    @RequestMapping(value = "/services/{serviceId}/instance", method = RequestMethod.GET)
    GrayInstance getInstance(@PathVariable("serviceId") String serviceId,
                             @RequestParam("instanceId") String instanceId);


    @RequestMapping(value = "/services/{serviceId}/instance", method = RequestMethod.DELETE)
    ResponseEntity<Void> delInstance(@PathVariable("serviceId") String serviceId,
                                     @RequestParam("instanceId") String instanceId);

    @RequestMapping(value = "/services/{serviceId}/instance", method = RequestMethod.POST)
    ResponseEntity<Void> instance(@PathVariable("serviceId") String serviceId, @RequestBody GrayInstance instance);


    @RequestMapping(value = "/services/{serviceId}/instance/policyGroups", method = RequestMethod.GET)
    List<PolicyDefinition> policyGroups(@PathVariable("serviceId") String serviceId,
                                        @RequestParam("instanceId") String instanceId);


    @RequestMapping(value = "/services/{serviceId}/instance/policyGroups/{groupId}",
            method = RequestMethod.GET)
    PolicyDefinition policyGroup(@PathVariable("serviceId") String serviceId,
                                 @RequestParam("instanceId") String instanceId,
                                 @PathVariable("groupId") String groupId);


    @RequestMapping(value = "/services/{serviceId}/instance/policyGroups/{groupId}/policies",
            method = RequestMethod.GET)
    List<DecisionDefinition> policies(@PathVariable("serviceId") String serviceId,
                                      @RequestParam("instanceId") String instanceId,
                                      @PathVariable("groupId") String groupId);


    @RequestMapping(value = "/services/{serviceId}/instance/policyGroups/{groupId}/policies/{policyId}",
            method = RequestMethod.GET)
    DecisionDefinition policy(@PathVariable("serviceId") String serviceId,
                              @RequestParam("instanceId") String instanceId,
                              @PathVariable("groupId") String groupId,
                              @PathVariable("policyId") String policyId);
}
