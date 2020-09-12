package cn.springcloud.gray.client.dubbo.servernode;

import org.springframework.cloud.client.ServiceInstance;

/**
 * @author saleson
 * @date 2020-09-11 02:16
 */
public interface ServiceInstanceIdExtractor {

    String getInstanceId(ServiceInstance serviceInstance);

}
