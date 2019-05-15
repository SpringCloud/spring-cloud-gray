package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimpleGrayManager extends AbstractGrayManager {


    protected Map<String, GrayService> grayServices = new ConcurrentHashMap<>();

    public SimpleGrayManager(GrayDecisionFactoryKeeper grayDecisionFactoryKeeper, List<RequestInterceptor> requestInterceptors) {
        super(grayDecisionFactoryKeeper, requestInterceptors);
    }


    @Override
    public boolean hasGray(String serviceId) {
        GrayService grayService = grayServices.get(serviceId);
        return grayService != null && !grayService.getGrayInstances().isEmpty();
    }

    @Override
    public Collection<GrayService> allGrayServices() {
        return grayServices.values();
    }

    @Override
    public GrayService getGrayService(String serviceId) {
        return grayServices.get(serviceId);
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        GrayService service = getGrayService(serviceId);
        return service != null ? service.getGrayInstance(instanceId) : null;
    }


    @Override
    public void updateGrayInstance(GrayInstance instance) {
        if (instance == null || !instance.isGray()) {
            return;
        }
        updateGrayInstance(grayServices, instance);
    }

    protected void updateGrayInstance(Map<String, GrayService> grayServices, GrayInstance instance) {
        GrayService service = getGrayService(instance.getServiceId());
        if (service == null) {
            synchronized (this) {
                service = getGrayService(instance.getServiceId());
                if (service == null) {
                    service = new GrayService();
                    service.setServiceId(instance.getServiceId());
                    grayServices.put(service.getServiceId(), service);
                }
            }
        } else if (grayServices != this.grayServices) {
            grayServices.put(service.getServiceId(), service);
        }
        log.debug("添加灰度实例, serviceId:{}, instanceId:{}", instance.getServiceId(), instance.getInstanceId());
        service.setGrayInstance(instance);
    }

    @Override
    public void closeGray(GrayInstance instance) {
        GrayService service = getGrayService(instance.getServiceId());
        if (service == null) {
            log.debug("没有找到灰度服务:{}, 所以无需删除实例:{} 的灰度状态", instance.getServiceId(), instance.getInstanceId());
            return;
        }
        log.debug("关闭实例的在灰度状态, serviceId:{}, instanceId:{}", instance.getServiceId(), instance.getInstanceId());
        service.removeGrayInstance(instance.getInstanceId());
    }

    @Override
    public void closeGray(String serviceId, String instanceId) {
        GrayService service = getGrayService(serviceId);
        if (service == null) {
            log.debug("没有找到灰度服务:{}, 所以无需删除实例:{} 的灰度状态", serviceId, instanceId);
            return;
        }
        log.debug("关闭实例的在灰度状态, serviceId:{}, instanceId:{}", serviceId, instanceId);
        service.removeGrayInstance(instanceId);
    }


    @Override
    public void shutdown() {

    }
}
