package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class EurekaInstanceLocalInfoObtainer implements InstanceLocalInfoObtainer, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private InstanceLocalInfo instanceLocalInfo;


    @Override
    public InstanceLocalInfo getInstanceLocalInfo() {
        if (instanceLocalInfo == null) {
            EurekaInstanceConfig eurekaInstanceConfig = applicationContext.getBean(EurekaInstanceConfig.class);
            String instanceId = eurekaInstanceConfig.getInstanceId();

            int port = eurekaInstanceConfig.getNonSecurePort();
            if (eurekaInstanceConfig.getSecurePortEnabled()) {
                port = eurekaInstanceConfig.getSecurePort();
            }
            instanceLocalInfo = InstanceLocalInfo.builder()
                    .instanceId(instanceId)
                    .serviceId(eurekaInstanceConfig.getAppname())
                    .host(eurekaInstanceConfig.getHostName(false))
                    .port(port)
                    .build();
        }
        return instanceLocalInfo;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
