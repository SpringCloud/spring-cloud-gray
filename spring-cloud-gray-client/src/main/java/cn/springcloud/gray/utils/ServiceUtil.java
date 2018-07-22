package cn.springcloud.gray.utils;

import com.netflix.loadbalancer.Server;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import java.util.Map;

/**
 * @Author: duozl
 * @Date: 2018/6/5 15:56
 */
public class ServiceUtil {
    private static final String METADATA_KEY_INSTANCE_ID = "instanceId";

    public static String getInstanceId(Server server, Map<String, String> serverMetadata) {
        try {
            if (server instanceof ZookeeperServer) {
                if (serverMetadata.containsKey(METADATA_KEY_INSTANCE_ID)) {
                    return serverMetadata.get(METADATA_KEY_INSTANCE_ID);
                } else {
                    throw new IllegalStateException("Unable to find config spring.cloud.zookeeper.discovery.metadata" +
                            ".instanceId!");
                }
            }
        } catch (Throwable e) {
            // do nothing，可能是类找不到等原因，如果引入了zookeeper的依赖，这个不会找不到
        }
        return server.getMetaInfo().getInstanceId();
    }
}
