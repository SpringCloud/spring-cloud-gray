package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class EurekaInstanceDiscoveryClient implements ApplicationContextAware, InitializingBean, InstanceDiscoveryClient {

    private EurekaServiceRegistry eurekaServiceRegistry;
    private EurekaRegistration eurekaRegistration;
    private ApplicationContext applicationContext;



    @Override
    public void setStatus(InstanceStatus status) {
        eurekaServiceRegistry.setStatus(eurekaRegistration, EurekaInstatnceTransformer.toEurekaInstanceStatus(status).name());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eurekaServiceRegistry = applicationContext.getBean(EurekaServiceRegistry.class);
        eurekaRegistration = applicationContext.getBean(EurekaRegistration.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
