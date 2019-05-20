package cn.springcloud.gray.service.test;

import cn.springcloud.gray.server.app.GrayServerApplication;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayServerApplication.class)
@Slf4j
public class EurekaDiscoveryTest {


    @Autowired
    private EurekaServiceRegistry eurekaServiceRegistry;
    @Autowired
    private EurekaClient eurekaClient;


    @Test
    public void test() {
        Application application = eurekaClient.getApplication("service-a");
        application.getInstances()
                .forEach(instanceInfo -> log.info("{}:{}  {}", instanceInfo.getInstanceId(), instanceInfo.getStatus(), instanceInfo));
    }

}
