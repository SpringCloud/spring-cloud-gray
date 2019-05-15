package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.client.netflix.EurekaServerExplainer;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EurekaClient.class)
public class GrayClientEurekaAutoConfiguration {

  @Autowired private SpringClientFactory springClientFactory;

  @Bean
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
}
