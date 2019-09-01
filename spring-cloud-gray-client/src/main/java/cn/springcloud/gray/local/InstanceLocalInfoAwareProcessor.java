package cn.springcloud.gray.local;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstanceLocalInfoAwareProcessor implements BeanPostProcessor {

    private  InstanceLocalInfo instanceLocalInfo;

    public InstanceLocalInfoAwareProcessor(InstanceLocalInfo instanceLocalInfo) {
        this.instanceLocalInfo = instanceLocalInfo;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InstanceLocalInfoAware) {
            ((InstanceLocalInfoAware) bean).setInstanceLocalInfo(instanceLocalInfo);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
