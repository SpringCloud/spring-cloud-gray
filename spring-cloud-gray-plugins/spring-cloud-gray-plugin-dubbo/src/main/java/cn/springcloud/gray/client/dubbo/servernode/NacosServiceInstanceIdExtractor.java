package cn.springcloud.gray.client.dubbo.servernode;

import cn.springcloud.gray.utils.StringUtils;
import org.springframework.cloud.client.ServiceInstance;

/**
 * @author saleson
 * @date 2020-09-11 10:34
 */
public class NacosServiceInstanceIdExtractor implements ServiceInstanceIdExtractor {
    @Override
    public String getInstanceId(ServiceInstance serviceInstance) {
        String id = serviceInstance.getInstanceId();
        if (StringUtils.isEmpty(id)) {
            id = serviceInstance.getMetadata().get("nacos.instanceId");
        }
        return id;
    }
}
