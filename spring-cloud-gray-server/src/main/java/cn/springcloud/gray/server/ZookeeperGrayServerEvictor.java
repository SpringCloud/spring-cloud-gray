package cn.springcloud.gray.server;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.core.GrayServiceManager;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Collection;
import java.util.List;


/**
 * 依赖EurekaClient来检查服务实例是否下线
 */
public class ZookeeperGrayServerEvictor implements GrayServerEvictor {

    private DiscoveryClient discoveryClient;

    public ZookeeperGrayServerEvictor(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void evict(GrayServiceManager serviceManager) {
        Collection<GrayService> grayServices = serviceManager.allGrayService();
        grayServices.forEach(grayService -> {
            grayService.getGrayInstances().forEach(grayInstance -> {
                evict(serviceManager, grayInstance);
            });
        });

    }


    private void evict(GrayServiceManager serviceManager, GrayInstance grayInstance) {
        if (isDownline(grayInstance)) {
            serviceManager.deleteGrayInstance(grayInstance.getServiceId(), grayInstance.getInstanceId());
        }
    }


    private boolean isDownline(GrayInstance grayInstance) {
        String serviceId = grayInstance.getServiceId();
        String instanceId = grayInstance.getInstanceId();
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (null == instances || instances.isEmpty()) {
            return true;
        }

        return instances.stream()
                .anyMatch(instance -> instanceId.equals(instance.getHost() + ":" + instance.getPort()));
    }
}
