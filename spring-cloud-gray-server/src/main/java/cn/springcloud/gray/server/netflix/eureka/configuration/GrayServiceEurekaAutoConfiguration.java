package cn.springcloud.gray.server.netflix.eureka.configuration;

/**
 * @Author: duozl
 * @Date: 2018/6/4 18:59
 */

import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.netflix.eureka.EurekaGrayServerEvictor;
import com.netflix.discovery.EurekaClient;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EurekaClient.class)
public class GrayServiceEurekaAutoConfiguration {

//    @Autowired
//    private EurekaClient eurekaClient;
//    @Autowired
//    private DiscoveryClient discoveryClient;


    @Bean
    public GrayServerEvictor grayServerEvictor(EurekaClient eurekaClient) {
        return new EurekaGrayServerEvictor(eurekaClient);
    }
}
