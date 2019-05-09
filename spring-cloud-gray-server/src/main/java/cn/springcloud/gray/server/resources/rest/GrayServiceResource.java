package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.core.*;
import cn.springcloud.gray.server.api.GrayServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class GrayServiceResource implements GrayServiceApi {
    @Autowired
    private GrayServiceManager grayServiceManager;


    @Override
    public List<GrayService> services() {
        return new ArrayList<>(grayServiceManager.allGrayService());
    }

    @Override
    public List<GrayService> enableServices() {
        Collection<GrayService> grayServices = grayServiceManager.allGrayService();
        List<GrayService> serviceList = new ArrayList<>(grayServices.size());
        for (GrayService grayService : grayServices) {
            if (grayService.isOpenGray()) {
                serviceList.add(grayService.takeNewOpenGrayService());
            }
        }

        return serviceList;
    }

    @Override
    public GrayService service(@PathVariable("serviceId") String serviceId) {
        return grayServiceManager.getGrayService(serviceId);
    }

    @Override
    public List<GrayInstance> instances(@PathVariable("serviceId") String serviceId) {
        return grayServiceManager.getGrayService(serviceId).getGrayInstances();
    }

    @Override
    public GrayInstance getInstance(@PathVariable("serviceId") String serviceId, String instanceId) {
        return grayServiceManager.getGrayInstane(serviceId, instanceId);
    }

    @Override
    public ResponseEntity<Void> delInstance(@PathVariable("serviceId") String serviceId, String instanceId) {
        grayServiceManager.deleteGrayInstance(serviceId, instanceId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> instance(@PathVariable("serviceId") String serviceId, @RequestBody GrayInstance instance) {
        instance.setServiceId(serviceId);
        grayServiceManager.addGrayInstance(instance);
        return ResponseEntity.ok().build();
    }


    @Override
    public List<PolicyDefinition> policyGroups(@PathVariable("serviceId") String serviceId, String instanceId) {
        return grayServiceManager.getGrayInstane(serviceId, instanceId).getPolicyDefinitions();
    }

    @Override
    public PolicyDefinition policyGroup(@PathVariable("serviceId") String serviceId, String instanceId,
                                        @PathVariable("groupId") String groupId) {
        return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId);
    }

    @Override
    public List<DecisionDefinition> policies(@PathVariable("serviceId") String serviceId, String instanceId,
                                             @PathVariable("groupId") String groupId) {
        return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId).getList();
    }

    @Override
    public DecisionDefinition policy(@PathVariable("serviceId") String serviceId, String instanceId,
                                     @PathVariable("groupId") String groupId, @PathVariable("policyId") String policyId) {
        return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId).getGrayPolicy(policyId);
    }
}
