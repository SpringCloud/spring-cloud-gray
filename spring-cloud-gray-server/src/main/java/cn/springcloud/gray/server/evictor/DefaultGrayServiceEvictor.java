package cn.springcloud.gray.server.evictor;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.discovery.ServiceDiscover;
import cn.springcloud.gray.server.discovery.ServiceInfo;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.module.domain.GrayService;

import java.util.List;
import java.util.Objects;

public class DefaultGrayServiceEvictor implements GrayServerEvictor {
    private ServiceDiscover serviceDiscover;

    public DefaultGrayServiceEvictor(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    @Override
    public void evict(GrayServerModule grayServerModule) {
        grayServerModule.allGrayServices().forEach(grayService -> {
            ServiceInfo serviceInfo = serviceDiscover.getServiceInfo(grayService.getServiceId());
            if (serviceInfo == null) {
                downAllInstance(grayServerModule, grayService);
            } else {
                List<GrayInstance> grayInstances = grayServerModule.listGrayInstancesByServiceId(grayService.getServiceId());
                grayInstances.forEach(grayInstance -> {
                    InstanceInfo instanceInfo = serviceDiscover.getInstanceInfo(grayInstance.getServiceId(), grayInstance.getInstanceId());
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
        List<GrayInstance> grayInstances = grayServerModule.listGrayInstancesByServiceId(grayService.getServiceId(), InstanceStatus.UP);
        grayInstances.forEach(i -> grayServerModule.instanceShutdown(i.getInstanceId()));
    }


    private InstanceStatus getInstanceStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return InstanceStatus.DOWN;
        }
        return instanceInfo.getInstanceStatus();
    }
}
