package cn.springcloud.gray.service.test;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;

//@ActiveProfiles({"dev"})
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GrayServerApplication.class)
@Slf4j
public class EurekaDiscoveryTest {


    @Autowired
    private EurekaServiceRegistry eurekaServiceRegistry;
    @Autowired
    private EurekaClient eurekaClient;


    //    @Test
    public void test() {
        Application application = eurekaClient.getApplication("service-a");
        application.getInstances()
                .forEach(instanceInfo -> log.info("{}:{}  {}", instanceInfo.getInstanceId(), instanceInfo.getStatus(), instanceInfo));
    }

}
