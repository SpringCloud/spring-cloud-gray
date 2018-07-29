package cn.springcloud.gray.client;

import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.core.GrayManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;
import javax.naming.ConfigurationException;

public class GrayClientInitializingBean implements InitializingBean, ApplicationContextAware {
    private ApplicationContext cxt;

    @Override
    public void afterPropertiesSet() throws Exception {
//        registrShutdownFunc();
    }

    @EventListener(InstanceRegisteredEvent.class)
    public void initializingBean(InstanceRegisteredEvent event) throws ConfigurationException {
        GrayClientAppContext.setGrayManager(cxt.getBean(GrayManager.class));
        GrayClientAppContext.setInstanceLocalInfo(instanceLocalInfo());
        GrayClientAppContext.setGrayClientProperties(cxt.getBean(GrayClientProperties.class));

        startForWork();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
    }

//    private void registrShutdownFunc(){
//        Runtime.getRuntime().addShutdownHook(new Thread(()->{
//            shutdown();
//        }));
//    }

    @PreDestroy
    public void shutdown() {
        GrayClientAppContext.getGrayManager().serviceDownline();
    }

    private void startForWork() {
        GrayClientAppContext.getGrayManager().openForWork();
    }

    private InstanceLocalInfo instanceLocalInfo() throws ConfigurationException {
        Registration registration = cxt.getBean(Registration.class);
        if (registration.getClass().getSimpleName().equals("EurekaRegistration")) {
            return InstanceLocalInfoFromEureka.instanceLocalInfoFromEureka(registration);
        } else if (registration.getClass().getSimpleName().equals("ServiceInstanceRegistration")) {
            return InstanceLocalInfoFromZookeeper.instanceLocalInfoFromZookeeper(cxt, registration);
        }
        throw new ConfigurationException("Un yet supported Registration:"
                + registration.getClass().getSimpleName() + "!");
    }
}