package cn.springcloud.gray.utils;

import com.netflix.loadbalancer.Server;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;

/**
 * @Author: duozl
 * @Date: 2018/6/5 15:56
 */
public class ServiceUtil {

    public static String getInstanceId(Server server) {
        String instanceId;
        try {
            if (server instanceof ZookeeperServer) {
                return server.getHostPort();
            }
        } catch (Exception e) {
            // do nothing，可能是类找不到等原因，如果引入了zookeeper的依赖，这个不会找不到
        }
        instanceId = server.getMetaInfo().getInstanceId();
        return instanceId;
    }

    public static String getInstanceId(Registration registration) {
        String instanceId = null;
        try {
            if (registration instanceof ZookeeperRegistration) {
                ServiceInstance<ZookeeperInstance> instance = ((ZookeeperRegistration) registration)
                        .getServiceInstance();
                instanceId = instance.getAddress() + ":" + instance.getPort();
            }
        } catch (Throwable e) {
            // do nothing，可能是类找不到等原因，如果引入了zookeeper的依赖，这个不会找不到
        }

        try {
            if (registration instanceof EurekaRegistration) {
                instanceId = ((EurekaRegistration) registration).getInstanceConfig().getInstanceId();
            }
        } catch (Throwable e) {
            // do nothing，可能是类找不到等原因，如果引入了eureka的依赖，这个不会找不到
        }

        return instanceId;
    }
}
