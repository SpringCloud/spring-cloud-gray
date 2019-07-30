package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoAware;
import cn.springcloud.gray.local.InstanceLocalInfoAwareProcessor;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(GrayManager.class)
public class GrayClientBeanPostProcessorConfiguration {

    @Bean
    @ConditionalOnProperty(value = "gray.aware.instanceLocalInfo.enabled", matchIfMissing = true)
    public InstanceLocalInfoAwareProcessor instanceLocalInfoAwareProcessor(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer) {
        return new InstanceLocalInfoAwareProcessor(instanceLocalInfoInitiralizer.getInstanceLocalInfo());
    }



}
