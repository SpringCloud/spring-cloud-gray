package cn.springcloud.gray.server.netflix.eureka.configuration;

/**
 * @Author: duozl
 * @Date: 2018/6/4 18:59
 */

import cn.springcloud.gray.server.discovery.InstanceInfoAnalyser;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.netflix.eureka.EurekaServiceDiscovery;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//import org.springframework.cloud.client.discovery.DiscoveryClient;

@Configuration
@ConditionalOnClass(EurekaClient.class)
@AutoConfigureAfter()
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
    public ServiceDiscovery serviceDiscover(
            EurekaClient eurekaClient,
            List<InstanceInfoAnalyser<InstanceInfo>> instanceInfoAnalysers) {
        return new EurekaServiceDiscovery(eurekaClient, instanceInfoAnalysers);
    }
}
