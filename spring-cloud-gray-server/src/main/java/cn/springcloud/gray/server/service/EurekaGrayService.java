package cn.springcloud.gray.server.service;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayPolicyGroup;
import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.resources.domain.vo.GrayInstanceVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayPolicyGroupVO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration")
public class EurekaGrayService extends AbstractGrayService {

    private final EurekaClient eurekaClient;
    private final GrayServiceManager grayServiceManager;

    @Autowired
    public EurekaGrayService(EurekaClient eurekaClient, DiscoveryClient discoveryClient,
                             GrayServiceManager grayServiceManager) {
        super(grayServiceManager, discoveryClient);
        this.eurekaClient = eurekaClient;
        this.grayServiceManager = grayServiceManager;
    }

    /**
     * 返回服务实例列表
     *
     * @param serviceId 服务id
     * @return 灰度服务实例VO列表
     */
    @Override
    public ResponseEntity<List<GrayInstanceVO>> instances(String serviceId) {
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


    /**
     * 服务实例的所有灰度策略组
     *
     * @param serviceId  服务id
     * @param instanceId 实例id
     * @return 灰策略组VO列表
     */
    @Override
    public ResponseEntity<List<GrayPolicyGroupVO>> policyGroups(String serviceId, String instanceId) {

        Application app = eurekaClient.getApplication(serviceId);
        InstanceInfo instanceInfo = app.getByInstanceId(instanceId);
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        String appName = instanceInfo.getAppName();
        String homePageUrl = instanceInfo.getHomePageUrl();
        if (grayInstance != null && grayInstance.getGrayPolicyGroups() != null) {
            List<GrayPolicyGroup> policyGroups = grayInstance.getGrayPolicyGroups();
            List<GrayPolicyGroupVO> vos = new ArrayList<>(policyGroups.size());
            for (GrayPolicyGroup policyGroup : policyGroups) {
                vos.add(getPolicyGroup(serviceId, appName, instanceId, homePageUrl, policyGroup));
            }
            return ResponseEntity.ok(vos);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


    /**
     * 灰度策略组
     *
     * @param serviceId  服务id
     * @param instanceId 实例id
     * @param groupId    灰度策略组id
     * @return 灰度策略组VO
     */
    @Override
    public ResponseEntity<GrayPolicyGroupVO> policyGroup(String serviceId, String instanceId, String groupId) {
        Application app = eurekaClient.getApplication(serviceId);
        InstanceInfo instanceInfo = app.getByInstanceId(instanceId);
        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        String appName = instanceInfo.getAppName();
        String homePageUrl = instanceInfo.getHomePageUrl();
        if (grayInstance != null) {
            GrayPolicyGroup policyGroup = grayInstance.getGrayPolicyGroup(groupId);
            if (policyGroup != null) {
                return ResponseEntity.ok(getPolicyGroup(serviceId, appName, instanceId, homePageUrl, policyGroup));
            }
        }
        return ResponseEntity.ok().build();
    }
}
