package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.client.netflix.eureka.EurekaInstanceDiscoveryClient;
import cn.springcloud.gray.client.netflix.eureka.EurekaServerExplainer;
import cn.springcloud.gray.servernode.InstanceDiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean({GrayManager.class})
public class GrayClientEurekaAutoConfiguration {


    @ConditionalOnBean({EurekaClient.class})
    public static class GrayEurekaClientConfiguraion {
        @Autowired
        private SpringClientFactory springClientFactory;

        @Bean
        @ConditionalOnBean({EurekaRegistration.class})
        @ConditionalOnMissingBean
        public InstanceLocalInfo instanceLocalInfo(@Autowired EurekaRegistration registration) {
            String instanceId = registration.getInstanceConfig().getInstanceId();

            return InstanceLocalInfo.builder()
                    .instanceId(instanceId)
                    .serviceId(registration.getServiceId())
                    .host(registration.getHost())
                    .port(registration.getPort())
                    .build();
        }

        @Bean
        @ConditionalOnMissingBean
        public EurekaServerExplainer eurekaServerExplainer() {
            return new EurekaServerExplainer(springClientFactory);
        }


        @Bean
        @ConditionalOnBean({EurekaServiceRegistry.class, EurekaRegistration.class})
        public InstanceDiscoveryClient instanceDiscoveryClient(
                EurekaServiceRegistry eurekaServiceRegistry, EurekaRegistration eurekaRegistration) {
            return new EurekaInstanceDiscoveryClient(eurekaServiceRegistry, eurekaRegistration);
        }
    }

}
