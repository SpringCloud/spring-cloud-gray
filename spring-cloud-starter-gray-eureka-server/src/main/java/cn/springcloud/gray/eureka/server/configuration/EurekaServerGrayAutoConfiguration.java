package cn.springcloud.gray.eureka.server.configuration;

import cn.springcloud.gray.bean.properties.EnableConfigurationProperties;
import cn.springcloud.gray.eureka.server.communicate.GrayCommunicateClient;
import cn.springcloud.gray.eureka.server.communicate.HttpCommunicateClient;
import cn.springcloud.gray.eureka.server.communicate.RetryableGrayCommunicateClient;
import cn.springcloud.gray.eureka.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.eureka.server.listener.EurekaInstanceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrayServerProperties.class)
@ConditionalOnProperty("gray.server.url")
public class EurekaServerGrayAutoConfiguration {


    @Autowired
    private GrayServerProperties grayServerProperties;

    @Bean
    @ConditionalOnMissingBean
    public GrayCommunicateClient grayCommunicateClient() {
        GrayCommunicateClient communicateClient = new HttpCommunicateClient(grayServerProperties.getUrl());
        if (grayServerProperties.isRetryable()) {
            return new RetryableGrayCommunicateClient(grayServerProperties.getRetryNumberOfRetries(), communicateClient);
        }
        return communicateClient;
    }


    @Bean
    public EurekaInstanceListener eurekaInstanceListener(GrayCommunicateClient communicateClient) {
        return new EurekaInstanceListener(communicateClient);
    }


}
