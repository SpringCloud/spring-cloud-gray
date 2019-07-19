package cn.springcloud.gray.service.b.configuration;

import cn.springcloud.gray.service.b.eureka.GrayEurekaEventListener;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
public class EurekaConfiguration implements InitializingBean {

    @Autowired
    private EurekaClient eurekaClient;

//    @Bean
//    public ServerListProcessor serverListProcessor() {
//        return (serviceId, servers) -> {
//            Application application = eurekaClient.getApplication(serviceId);
//            if (Objects.isNull(application)) {
//                return servers;
//            }
//            List<Server> unUpServers = application.getInstancesAsIsFromEureka().stream()
//                    .filter(instanceInfo -> instanceInfo.getStatus() == InstanceInfo.InstanceStatus.STARTING)
//                    .map(instanceInfo -> new DiscoveryEnabledServer(instanceInfo, true))
//                    .collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(unUpServers)) {
//                return servers;
//            }
//            return ListUtils.union(servers, unUpServers);
//
//        };
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eurekaClient.registerEventListener(new GrayEurekaEventListener());
    }


}
