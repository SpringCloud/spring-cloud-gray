package cn.springcloud.gray.server.evictor;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.discovery.ServiceInfo;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayService;

import java.util.List;
import java.util.Objects;

public class DefaultGrayServiceEvictor implements GrayServerEvictor {
    private ServiceDiscovery serviceDiscovery;
    private GrayServerProperties grayServerProperties;

    public DefaultGrayServiceEvictor(GrayServerProperties grayServerProperties, ServiceDiscovery serviceDiscovery) {
        this.grayServerProperties = grayServerProperties;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void evict(GrayServerModule grayServerModule) {
        grayServerModule.allGrayServices().forEach(grayService -> {
            ServiceInfo serviceInfo = serviceDiscovery.getServiceInfo(grayService.getServiceId());
            if (serviceInfo == null) {
                downAllInstance(grayServerModule, grayService);
            } else {
                List<GrayInstance> grayInstances = grayServerModule.listGrayInstancesByServiceId(grayService.getServiceId());
                grayInstances.forEach(grayInstance -> {
                    InstanceInfo instanceInfo = serviceDiscovery.getInstanceInfo(grayInstance.getServiceId(), grayInstance.getInstanceId());
                    updateInstanceStatus(grayServerModule, grayInstance, instanceInfo);
                });
            }
        });
    }


    private void updateInstanceStatus(GrayServerModule grayServerModule, GrayInstance grayInstance, InstanceInfo
            instanceInfo) {
        InstanceStatus instanceStatus = instanceInfo == null ? InstanceStatus.DOWN : instanceInfo.getInstanceStatus();
        if (!Objects.equals(grayInstance.getInstanceStatus(), instanceStatus)) {
            grayServerModule.updateInstanceStatus(grayInstance.getInstanceId(), instanceStatus);
        }
    }

    private void downAllInstance(GrayServerModule grayServerModule, GrayService grayService) {
        List<GrayInstance> grayInstances =
                grayServerModule.listGrayInstancesByServiceId(grayService.getServiceId(),
                        grayServerProperties.getInstance().getNormalInstanceStatus());
        grayInstances.forEach(i -> grayServerModule.instanceShutdown(i.getInstanceId()));
    }


    private InstanceStatus getInstanceStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return InstanceStatus.DOWN;
        }
        return instanceInfo.getInstanceStatus();
    }
}
