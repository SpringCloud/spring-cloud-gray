package cn.springcloud.gray.server.service;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayPolicyGroup;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.resources.domain.fo.GrayPolicyGroupFO;
import cn.springcloud.gray.server.resources.domain.vo.GrayInstanceVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayPolicyGroupVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayServiceVO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGrayService {

    private GrayServiceManager grayServiceManager;
    private DiscoveryClient discoveryClient;

    public AbstractGrayService(GrayServiceManager grayServiceManager, DiscoveryClient discoveryClient) {
        this.grayServiceManager = grayServiceManager;
        this.discoveryClient = discoveryClient;
    }

    /**
     * 返回所有服务
     *
     * @return 灰度服务VO集合
     */
    public ResponseEntity<List<GrayServiceVO>> services() {
        List<String> serviceIds = discoveryClient.getServices();
        List<GrayServiceVO> services = new ArrayList<>(serviceIds.size());
        for (String serviceId : serviceIds) {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            if (null == instances || instances.isEmpty()) {
                continue;
            }
            GrayServiceVO vo = new GrayServiceVO();
            vo.setServiceId(serviceId);
            vo.setAppName(serviceId);
            vo.setInstanceSize(instances.size());
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
    public abstract ResponseEntity<List<GrayInstanceVO>> instances(String serviceId);

    public ResponseEntity<Void> editInstanceStatus(String serviceId, String instanceId, int status) {

        return grayServiceManager.updateInstanceStatus(serviceId, instanceId, status)
                ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    /**
     * 服务实例的所有灰度策略组
     *
     * @param serviceId  服务id
     * @param instanceId 实例id
     * @return 灰策略组VO列表
     */
    public abstract ResponseEntity<List<GrayPolicyGroupVO>> policyGroups(String serviceId, String instanceId);


    /**
     * 灰度策略组
     *
     * @param serviceId  服务id
     * @param instanceId 实例id
     * @param groupId    灰度策略组id
     * @return 灰度策略组VO
     */
    public abstract ResponseEntity<GrayPolicyGroupVO> policyGroup(String serviceId, String instanceId, String groupId);


    public ResponseEntity<Void> editPolicyGroupStatus(String serviceId, String instanceId, String groupId, int enable) {

        return grayServiceManager.updatePolicyGroupStatus(serviceId, instanceId, groupId, enable)
                ? ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }


    /**
     * 添加策略组
     *
     * @param serviceId     服务id
     * @param policyGroupFO 灰度策略组FO
     * @return Void
     */
    public ResponseEntity<Void> policyGroup(String serviceId, GrayPolicyGroupFO policyGroupFO) {
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
     * @param serviceId     服务id
     * @param instanceId    实例id
     * @param policyGroupId 灰度策略组id
     * @return Void
     */
    public ResponseEntity<Void> delPolicyGroup(String serviceId, String instanceId, String policyGroupId) {
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (grayInstance != null) {
            if (grayInstance.removeGrayPolicyGroup(policyGroupId) != null && grayInstance.getGrayPolicyGroups()
                    .isEmpty()) {
                grayServiceManager.deleteGrayInstance(serviceId, instanceId);
            }
        }

        return ResponseEntity.ok().build();
    }


    protected GrayPolicyGroupVO getPolicyGroup(String serviceId, String appName, String instanceId, String homePageUrl,
                                               GrayPolicyGroup policyGroup) {
        GrayPolicyGroupVO vo = new GrayPolicyGroupVO();
        vo.setAppName(appName);
        vo.setInstanceId(instanceId);
        vo.setServiceId(serviceId);
        vo.setUrl(homePageUrl);
        vo.setAlias(policyGroup.getAlias());
        vo.setPolicyGroupId(policyGroup.getPolicyGroupId());
        vo.setPolicies(policyGroup.getList());
        vo.setEnable(policyGroup.isEnable());
        return vo;
    }

}
