package cn.springcloud.gray.client.dubbo.servernode;

import org.apache.dubbo.rpc.Invoker;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-10 12:39
 */
public class DubboServerMetadataExtractor implements ServerMetadataExtractor<Invoker> {

    private ServiceInstanceExtractor serviceInstanceExtractor;

    public DubboServerMetadataExtractor(ServiceInstanceExtractor serviceInstanceExtractor) {
        this.serviceInstanceExtractor = serviceInstanceExtractor;
    }

    @Override
    public Map<String, String> getMetadata(Invoker invoker, String serviceId, String instanceId) {
        ServiceInstance serviceInstance = serviceInstanceExtractor.getServiceInstance(serviceId, instanceId);
        if (Objects.isNull(serviceInstance)) {
            return Collections.emptyMap();
        }
        return serviceInstance.getMetadata();
    }
}
