package cn.springcloud.gray.client.dubbo.listener;

import cn.springcloud.gray.client.dubbo.servernode.ServiceInstanceExtractor;
import cn.springcloud.gray.client.initialize.GrayInitializedEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * @author saleson
 * @date 2020-09-10 18:00
 */
public class ServiceInstanceLoadListener implements ApplicationListener<GrayInitializedEvent> {
    private ServiceInstanceExtractor serviceInstanceExtractor;
    private DiscoveryClient discoveryClient;

    public ServiceInstanceLoadListener(
            ServiceInstanceExtractor serviceInstanceExtractor,
            DiscoveryClient discoveryClient) {
        this.serviceInstanceExtractor = serviceInstanceExtractor;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void onApplicationEvent(GrayInitializedEvent event) {
        discoveryClient.getServices().forEach(this::loadServiceInstances);
    }


    private void loadServiceInstances(String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        serviceInstanceExtractor.putServiceInstance(serviceId, serviceInstances);
    }
}
