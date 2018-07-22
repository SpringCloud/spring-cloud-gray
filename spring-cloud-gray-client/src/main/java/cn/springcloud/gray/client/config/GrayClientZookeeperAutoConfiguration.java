package cn.springcloud.gray.client.config;

import cn.springcloud.gray.InstanceLocalInfo;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.zookeeper.ConditionalOnZookeeperEnabled;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.naming.ConfigurationException;

/**
 * @Author: duozl
 * @Date: 2018/6/5 18:18
 */
@AutoConfigureAfter(name = {"org.springframework.cloud.zookeeper.serviceregistry.ZookeeperAutoServiceRegistration"})
@ConditionalOnClass(name = "org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration")
public class GrayClientZookeeperAutoConfiguration {
    private static final String METADATA_KEY_INSTANCE_ID = "instanceId";

    @Bean
    @ConditionalOnMissingBean
    public InstanceLocalInfo instanceLocalInfo(@Autowired ZookeeperRegistration registration,
                                               @Autowired ZookeeperDiscoveryProperties properties)
            throws ConfigurationException {
        String instanceId;
        if (properties.getMetadata().containsKey(METADATA_KEY_INSTANCE_ID)) {
            instanceId = properties.getMetadata().get(METADATA_KEY_INSTANCE_ID);
        } else {
            throw new ConfigurationException("Unable to find config spring.cloud.zookeeper.discovery.metadata" +
                    ".instanceId!");
        }

        InstanceLocalInfo localInfo = new InstanceLocalInfo();
        localInfo.setInstanceId(instanceId);
        localInfo.setServiceId(registration.getServiceId());
        localInfo.setGray(false);
        return localInfo;
    }

    @EventListener(InstanceRegisteredEvent.class)
    public void bind(InstanceRegisteredEvent event) {

    }
}
