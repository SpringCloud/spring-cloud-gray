package cn.springcloud.gray.server.service;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayPolicyGroup;
import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.resources.domain.vo.GrayInstanceVO;
import cn.springcloud.gray.server.resources.domain.vo.GrayPolicyGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnClass(name = "org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration")
public class ZookeeperGrayService extends AbstractGrayService {
    private static final String METADATA_KEY_INSTANCE_ID = "instanceId";

    private final DiscoveryClient discoveryClient;
    private final GrayServiceManager grayServiceManager;

    @Autowired
    public ZookeeperGrayService(DiscoveryClient discoveryClient, GrayServiceManager grayServiceManager) {
        super(grayServiceManager, discoveryClient);
        this.discoveryClient = discoveryClient;
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
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (null != instances && !instances.isEmpty()) {
            instances.forEach(instance -> {
                String instanceId = getInstanceId(instance);
                GrayInstanceVO vo = new GrayInstanceVO();
                vo.setAppName(serviceId);
                vo.setServiceId(serviceId);
                vo.setInstanceId(instanceId);
                vo.setMetadata(instance.getMetadata());
                vo.setUrl(instance.getUri().toString());
                GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
                if (grayInstance != null) {
                    vo.setOpenGray(grayInstance.isOpenGray());
                    vo.setHasGrayPolicies(grayInstance.hasGrayPolicy());
                }
                list.add(vo);
            });
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
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (null == instances || instances.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        ServiceInstance serviceInstance = instances.stream()
                .filter(instance -> instanceId.equals(getInstanceId(instance)))
                .findFirst().orElse(null);

        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (null != serviceInstance && grayInstance != null && grayInstance.getGrayPolicyGroups() != null) {
            String homePageUrl = serviceInstance.getUri().toString();
            List<GrayPolicyGroup> policyGroups = grayInstance.getGrayPolicyGroups();
            List<GrayPolicyGroupVO> vos = new ArrayList<>(policyGroups.size());
            for (GrayPolicyGroup policyGroup : policyGroups) {
                vos.add(getPolicyGroup(serviceId, serviceId, instanceId, homePageUrl, policyGroup));
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
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (null == instances || instances.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        ServiceInstance serviceInstance = instances.stream()
                .filter(instance -> instanceId.equals(getInstanceId(instance)))
                .findFirst().orElse(null);

        GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceId);
        if (null != serviceInstance && grayInstance != null) {
            GrayPolicyGroup policyGroup = grayInstance.getGrayPolicyGroup(groupId);
            if (policyGroup != null) {
                String homePageUrl = serviceInstance.getUri().toString();
                return ResponseEntity.ok(getPolicyGroup(serviceId, serviceId, instanceId, homePageUrl, policyGroup));
            }
        }
        return ResponseEntity.ok().build();
    }

    private String getInstanceId(ServiceInstance instance) {
        if (instance.getMetadata().containsKey(METADATA_KEY_INSTANCE_ID)) {
            // 自定义instanceId
            return instance.getMetadata().get(METADATA_KEY_INSTANCE_ID);
        } else {
            org.apache.curator.x.discovery.ServiceInstance<ZookeeperInstance> serviceInstance =
                    ((ZookeeperServiceInstance) instance).getServiceInstance();
            return serviceInstance.getId();
        }
    }
}
