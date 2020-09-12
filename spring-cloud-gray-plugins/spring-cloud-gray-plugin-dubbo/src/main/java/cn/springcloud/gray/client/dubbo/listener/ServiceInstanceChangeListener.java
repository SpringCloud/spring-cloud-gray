package cn.springcloud.gray.client.dubbo.listener;

import cn.springcloud.gray.client.dubbo.servernode.ServiceInstanceExtractor;
import com.alibaba.cloud.dubbo.registry.event.ServiceInstancesChangedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author saleson
 * @date 2020-09-10 18:04
 */
public class ServiceInstanceChangeListener implements ApplicationListener<ServiceInstancesChangedEvent> {
    private ServiceInstanceExtractor serviceInstanceExtractor;

    public ServiceInstanceChangeListener(ServiceInstanceExtractor serviceInstanceExtractor) {
        this.serviceInstanceExtractor = serviceInstanceExtractor;
    }

    @Override
    public void onApplicationEvent(ServiceInstancesChangedEvent event) {
        String serviceId = event.getServiceName();
        serviceInstanceExtractor.removeService(serviceId);
        serviceInstanceExtractor.putServiceInstance(serviceId, event.getServiceInstances());
    }
}
