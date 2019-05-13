package cn.springcloud.gray.server.netflix.eureka;

import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.module.GrayModule;
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

    private void evict(GrayModule grayModule, InstanceInfo instanceInfo, GrayInstance grayInstance) {
        InstanceStatus instanceStatus = getInstanceStatus(instanceInfo);
        if (grayInstance.getInstanceStatus() != instanceStatus) {
            grayModule.updateInstanceStatus(grayInstance.getInstanceId(), instanceStatus);
        }
    }


    private InstanceStatus getInstanceStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return InstanceStatus.DOWN;
        }
        return null;
    }


    @Override
    public void evict(GrayModule grayModule) {
        grayModule.allGrayService().forEach(grayService -> {
            Application app = eurekaClient.getApplication(grayService.getServiceId());
            grayModule.listGrayInstanceBySerivceId(grayService.getServiceId()).forEach(instance -> {
                evict(grayModule, app.getByInstanceId(instance.getInstanceId()), instance);
            });

        });
    }
}
