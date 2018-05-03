package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayPolicyGroup;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.resources.domain.fo.GrayPolicyGroupFO;
import cn.springcloud.gray.server.resources.domain.vo.GrayInstanceVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayPolicyGroupVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayServiceVO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/gray/manager")
public class ServiceGrayResouce {

    @Autowired
    private Registration registration;
    @Autowired
    private EurekaClient eurekaClient;
    @Autowired
    private org.springframework.cloud.client.discovery.DiscoveryClient discoveryClient;
    @Autowired
    private GrayServiceManager grayServiceManager;


    /**
     * 返回所有服务
     *
     * @return 灰度服务VO集合
     */
    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ResponseEntity<List<GrayServiceVO>> services() {
        List<String> serviceIds = discoveryClient.getServices();
//        List<Application> apps = eurekaClient.getApplications().getRegisteredApplications();
        List<GrayServiceVO> services = new ArrayList<>(serviceIds.size());
        for (String serviceId : serviceIds) {
            GrayServiceVO vo = new GrayServiceVO();
            vo.setServiceId(serviceId);
            Application app = eurekaClient.getApplication(serviceId);
            vo.setAppName(app.getName());
            vo.setInstanceSize(app.getInstances().size());
            GrayService grayService = grayServiceManager.getGrayService(serviceId);
            if (grayService != null) {
                vo.setHasGrayInstances(grayService.isOpenGray());
                vo.setHasGrayPolicies(grayService.hasGrayPolicy());
            }
            services.add(vo);
        }
        return ResponseEntity.ok(services);
    }


    /**
     * 返回服务实例列表
     *
     * @param serviceId 服务id
     * @return 灰度服务实例VO列表
     */
    @RequestMapping(value = "/services/{serviceId}/instances", method = RequestMethod.GET)
    public ResponseEntity<List<GrayInstanceVO>> instances(@PathVariable("serviceId") String serviceId) {
        List<GrayInstanceVO> list = new ArrayList<>();
        Application app = eurekaClient.getApplication(serviceId);
        List<InstanceInfo> instanceInfos = app.getInstances();
        for (InstanceInfo instanceInfo : instanceInfos) {
            GrayInstanceVO vo = new GrayInstanceVO();
            vo.setAppName(app.getName());
            vo.setServiceId(serviceId);
            vo.setInstanceId(instanceInfo.getInstanceId());
            vo.setMetadata(instanceInfo.getMetadata());
            vo.setUrl(instanceInfo.getHomePageUrl());
            GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceInfo.getInstanceId());
            if (grayInstance != null) {
                vo.setOpenGray(grayInstance.isOpenGray());
                vo.setHasGrayPolicies(grayInstance.hasGrayPolicy());
            }
            list.add(vo);
        }
        return ResponseEntity.ok(list);
    }


    @ApiOperation(value = "更新实例灰度状态")
    @RequestMapping(value = "/services/{serviceId}/instance/status/{status}", method = RequestMethod.PUT)
    public ResponseEntity<Void> editInstanceStatus(
            @PathVariable("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId,
            @ApiParam("0:关闭, 1:启用") @PathVariable("status") int status) {

        return grayServiceManager.updateInstanceStatus(serviceId, instanceId, status)
                ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    /**
     * 服务实例的所有灰度策略组
     *
     * @param serviceId 服务id
     * @param instanceId 实例id
     * @return 灰策略组VO列表
     */
    @RequestMapping(value = "/services/{serviceId}/instance/policyGroups", method = RequestMethod.GET)
    public ResponseEntity<List<GrayPolicyGroupVO>> policyGroups(@PathVariable("serviceId") String serviceId,
                                                                @RequestParam("instanceId") String instanceId) {

        Application app = eurekaClient.getApplication(serviceId);
        InstanceInfo instanceInfo = app.getByInstanceId(instanceId);
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (grayInstance != null && grayInstance.getGrayPolicyGroups() != null) {
            List<GrayPolicyGroup> policyGroups = grayInstance.getGrayPolicyGroups();
            List<GrayPolicyGroupVO> vos = new ArrayList<>(policyGroups.size());
            for (GrayPolicyGroup policyGroup : policyGroups) {
                vos.add(getPolicyGroup(serviceId, instanceInfo, policyGroup));
            }
            return ResponseEntity.ok(vos);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


    /**
     * 灰度策略组
     *
     * @param serviceId 服务id
     * @param instanceId 实例id
     * @param groupId 灰度策略组id
     * @return 灰度策略组VO
     */
    @RequestMapping(value = "/services/{serviceId}/instance/policyGroup/{groupId}", method = RequestMethod.GET)
    public ResponseEntity<GrayPolicyGroupVO> policyGroup(@PathVariable("serviceId") String serviceId,
                                                         @RequestParam("instanceId") String instanceId,
                                                         @PathVariable("groupId") String groupId) {
        Application app = eurekaClient.getApplication(serviceId);
        InstanceInfo instanceInfo = app.getByInstanceId(instanceId);
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (grayInstance != null) {
            GrayPolicyGroup policyGroup = grayInstance.getGrayPolicyGroup(groupId);
            if (policyGroup != null) {
                return ResponseEntity.ok(getPolicyGroup(serviceId, instanceInfo, policyGroup));
            }
        }
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "更新实例策略组启用状态")
    @RequestMapping(value = "/services/{serviceId}/instance/policyGroup/{groupId}/status/{status}", method = RequestMethod.PUT)
    public ResponseEntity<Void> editPolicyGroupStatus(@PathVariable("serviceId") String serviceId,
                                                      @RequestParam("instanceId") String instanceId,
                                                      @PathVariable("groupId") String groupId,
                                                      @ApiParam("0:关闭, 1:启用") @PathVariable("status") int enable) {

        return grayServiceManager.updatePolicyGroupStatus(serviceId, instanceId, groupId, enable)
                ? ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }


    /**
     * 添加策略组
     *
     * @param serviceId 服务id
     * @param policyGroupFO 灰度策略组FO
     * @return Void
     */
    @RequestMapping(value = "/services/{serviceId}/instance/policyGroup", method = RequestMethod.POST)
    public ResponseEntity<Void> policyGroup(
            @PathVariable("serviceId") String serviceId,
            @RequestBody GrayPolicyGroupFO policyGroupFO) {
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, policyGroupFO.getInstanceId());
        if (grayInstance == null) {
            grayInstance = new GrayInstance();
            grayInstance.setServiceId(serviceId);
            grayInstance.setInstanceId(policyGroupFO.getInstanceId());
            grayServiceManager.addGrayInstance(grayInstance);
            grayInstance = grayServiceManager.getGrayInstane(serviceId, policyGroupFO.getInstanceId());
        }

        grayInstance.addGrayPolicyGroup(policyGroupFO.toGrayPolicyGroup());

        return ResponseEntity.ok().build();
    }


    /**
     * 删除策略组
     *
     * @param serviceId 服务id
     * @param instanceId 实例id
     * @param policyGroupId 灰度策略组id
     * @return Void
     */
    @ApiOperation("删除策略组")
    @RequestMapping(value = "/services/{serviceId}/instance/policyGroup", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delPolicyGroup(
            @PathVariable("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId,
            @RequestParam("groupId") String policyGroupId) {
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (grayInstance != null) {
            if (grayInstance.removeGrayPolicyGroup(policyGroupId) != null && grayInstance.getGrayPolicyGroups().isEmpty()) {
                grayServiceManager.deleteGrayInstance(serviceId, instanceId);
            }
        }

        return ResponseEntity.ok().build();
    }


    private GrayPolicyGroupVO getPolicyGroup(
            String serviceId, InstanceInfo instanceInfo, GrayPolicyGroup policyGroup) {
        GrayPolicyGroupVO vo = new GrayPolicyGroupVO();
        vo.setAppName(instanceInfo.getAppName());
        vo.setInstanceId(instanceInfo.getInstanceId());
        vo.setServiceId(serviceId);
        vo.setUrl(instanceInfo.getHomePageUrl());
        vo.setAlias(policyGroup.getAlias());
        vo.setPolicyGroupId(policyGroup.getPolicyGroupId());
        vo.setPolicies(policyGroup.getList());
        vo.setEnable(policyGroup.isEnable());
        return vo;
    }

}
