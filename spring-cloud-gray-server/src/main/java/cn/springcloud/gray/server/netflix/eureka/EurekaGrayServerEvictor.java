package cn.springcloud.gray.server.netflix.eureka;

import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.module.domain.InstanceStatus;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;


/**
 * 依赖EurekaClient来检查服务实例是否下线
 */
public class EurekaGrayServerEvictor implements GrayServerEvictor {

    private EurekaClient eurekaClient;


    public EurekaGrayServerEvictor(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    private void evict(GrayServerModule grayServerModule, InstanceInfo instanceInfo, GrayInstance grayInstance) {
        InstanceStatus instanceStatus = getInstanceStatus(instanceInfo);
        if (grayInstance.getInstanceStatus() != instanceStatus) {
            grayServerModule.updateInstanceStatus(grayInstance.getInstanceId(), instanceStatus);
        }
    }


    private InstanceStatus getInstanceStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return InstanceStatus.DOWN;
        }
        InstanceInfo.InstanceStatus status = instanceInfo.getStatus();
        if (status == InstanceInfo.InstanceStatus.UP) {
            return InstanceStatus.UP;
        }
        return InstanceStatus.UNKNOWN;
    }


    @Override
    public void evict(GrayServerModule grayServerModule) {
        grayServerModule.allGrayServices().forEach(grayService -> {
            Application app = eurekaClient.getApplication(grayService.getServiceId());
            if (app != null) {
                grayServerModule.listGrayInstancesBySerivceId(grayService.getServiceId()).forEach(instance -> {
                    evict(grayServerModule, app.getByInstanceId(instance.getInstanceId()), instance);
                });
            }
        });
    }
}
