package cn.springcloud.gray.local;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstanceLocalInfoAwareProcessor implements BeanPostProcessor {

    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    public InstanceLocalInfoAwareProcessor(InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InstanceLocalInfoAware) {
            ((InstanceLocalInfoAware) bean).setInstanceLocalInfo(instanceLocalInfoObtainer.getInstanceLocalInfo());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
