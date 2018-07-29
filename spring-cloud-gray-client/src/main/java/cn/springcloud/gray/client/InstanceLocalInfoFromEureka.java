package cn.springcloud.gray.client;

import cn.springcloud.gray.InstanceLocalInfo;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;

public class InstanceLocalInfoFromEureka {

    public static InstanceLocalInfo instanceLocalInfoFromEureka(Registration registration) {
        EurekaRegistration eurekaRegistration = (EurekaRegistration) registration;
        String instanceId = eurekaRegistration.getInstanceConfig().getInstanceId();
        String serviceId = eurekaRegistration.getServiceId();

        InstanceLocalInfo localInfo = new InstanceLocalInfo();
        localInfo.setInstanceId(instanceId);
        localInfo.setServiceId(serviceId);
        localInfo.setGray(false);
        return localInfo;
    }
}
