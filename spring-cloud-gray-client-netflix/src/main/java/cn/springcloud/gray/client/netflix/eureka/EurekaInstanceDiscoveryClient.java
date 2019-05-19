package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;

@Slf4j
public class EurekaInstanceDiscoveryClient implements InstanceDiscoveryClient {

    private EurekaServiceRegistry eurekaServiceRegistry;
    private EurekaRegistration eurekaRegistration;


    public EurekaInstanceDiscoveryClient(EurekaServiceRegistry eurekaServiceRegistry, EurekaRegistration eurekaRegistration) {
        this.eurekaServiceRegistry = eurekaServiceRegistry;
        this.eurekaRegistration = eurekaRegistration;
    }

    @Override
    public void setStatus(InstanceStatus status) {
        eurekaServiceRegistry.setStatus(eurekaRegistration, EurekaInstatnceTransformer.toEurekaInstanceStatus(status).name());
    }

}
