package cn.springcloud.gray.client;

import cn.springcloud.gray.InstanceLocalInfo;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.context.ApplicationContext;

public class InstanceLocalInfoFromZookeeper {
    private static final String METADATA_KEY_INSTANCE_ID = "instanceId";

    public static InstanceLocalInfo instanceLocalInfoFromZookeeper(ApplicationContext cxt, Registration registration) {
        ServiceInstanceRegistration serviceInstanceRegistration = (ServiceInstanceRegistration) registration;
        ServiceInstance<ZookeeperInstance> serviceInstance = serviceInstanceRegistration.getServiceInstance();
        ZookeeperDiscoveryProperties properties = cxt.getBean(ZookeeperDiscoveryProperties.class);

        String instanceId;
        if (properties.getMetadata().containsKey(METADATA_KEY_INSTANCE_ID)) {
            // 自定义instanceId
            instanceId = properties.getMetadata().get(METADATA_KEY_INSTANCE_ID);
        } else {
            instanceId = serviceInstance.getId();
        }

        InstanceLocalInfo localInfo = new InstanceLocalInfo();
        localInfo.setInstanceId(instanceId);
        localInfo.setServiceId(serviceInstanceRegistration.getServiceId());
        localInfo.setGray(false);
        return localInfo;
    }
}
