package cn.springcloud.gray.server.netflix.eureka.configuration;

/**
 * @Author: duozl
 * @Date: 2018/6/4 18:59
 */

import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.netflix.eureka.EurekaServiceDiscovery;
import com.netflix.discovery.EurekaClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import org.springframework.cloud.client.discovery.DiscoveryClient;

@Configuration
@ConditionalOnBean(EurekaClient.class)
public class GrayServiceEurekaAutoConfiguration {

//    @Autowired
//    private EurekaClient eurekaClient;
//    @Autowired
//    private DiscoveryClient discoveryClient;


//    @Bean
//    public GrayServerEvictor grayServerEvictor(EurekaClient eurekaClient) {
//        return new EurekaGrayServerEvictor(eurekaClient);
//    }

    @Bean
    public ServiceDiscovery serviceDiscover(EurekaClient eurekaClient) {
        return new EurekaServiceDiscovery(eurekaClient);
    }
}
