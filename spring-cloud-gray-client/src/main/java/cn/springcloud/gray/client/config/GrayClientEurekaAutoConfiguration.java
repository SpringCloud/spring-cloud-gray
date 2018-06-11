package cn.springcloud.gray.client.config;

import cn.springcloud.gray.InstanceLocalInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: duozl
 * @Date: 2018/6/5 18:18
 */
@Configuration
@ConditionalOnBean(EurekaClient.class)
public class GrayClientEurekaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InstanceLocalInfo instanceLocalInfo(@Autowired EurekaRegistration registration) {
        String instanceId = registration.getInstanceConfig().getInstanceId();

        InstanceLocalInfo localInfo = new InstanceLocalInfo();
        localInfo.setInstanceId(instanceId);
        localInfo.setServiceId(registration.getServiceId());
        localInfo.setGray(false);
        return localInfo;
    }
}
