package cn.springcloud.gray.client.config;

import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.InstanceLocalInfoAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayClientBeanPostProcessorConfiguration {


    @Bean
//    @ConditionalOnBean(InstanceLocalInfo.class)
    public BeanPostProcessor beanPostProcessor(InstanceLocalInfo instanceLocalInfo) {
        return new BeanPostProcessor() {
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
        };
    }

}
