package cn.springcloud.gray.service.b.configuration;

import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.service.b.eureka.GrayEurekaEventListener;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//@Configuration
public class EurekaConfiguration implements InitializingBean {

    @Autowired
    private EurekaClient eurekaClient;

    @Bean
    public ServerListProcessor serverListProcessor() {
        return (serviceId, servers) -> {
            Application application = eurekaClient.getApplication(serviceId);
            if (Objects.isNull(application)) {
                return servers;
            }
            List<Server> unUpServers = application.getInstancesAsIsFromEureka().stream()
                    .filter(instanceInfo -> instanceInfo.getStatus() == InstanceInfo.InstanceStatus.STARTING)
                    .map(instanceInfo -> new DiscoveryEnabledServer(instanceInfo, true))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(unUpServers)) {
                return servers;
            }
            return ListUtils.union(servers, unUpServers);

        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eurekaClient.registerEventListener(new GrayEurekaEventListener());
    }


}
