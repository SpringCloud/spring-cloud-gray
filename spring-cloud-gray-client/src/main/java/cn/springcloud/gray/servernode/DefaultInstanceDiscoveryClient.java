package cn.springcloud.gray.servernode;

import cn.springcloud.gray.model.InstanceStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultInstanceDiscoveryClient implements InstanceDiscoveryClient, ApplicationContextAware, InitializingBean{

    private ServiceRegistry<Registration> serviceRegistry;
    private Registration registration;
    private ApplicationContext applicationContext;



    @Override
    public void setStatus(InstanceStatus status) {
        serviceRegistry.setStatus(registration, status.name());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registration = applicationContext.getBean(Registration.class);
        serviceRegistry = applicationContext.getBean(ServiceRegistry.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
