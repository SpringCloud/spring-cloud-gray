package cn.springcloud.gray.client.config;

import cn.springcloud.gray.InstanceLocalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.ConfigurationException;

/**
 * @Author: duozl
 * @Date: 2018/6/5 18:18
 */
@Configuration
@ConditionalOnBean(ZookeeperRegistration.class)
public class GrayClientZookeeperAutoConfiguration {
    private static final String METADATA_KEY_INSTANCE_ID = "instanceId";

    @Bean
    @ConditionalOnMissingBean
    public InstanceLocalInfo instanceLocalInfo(@Autowired Registration registration,
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
}
