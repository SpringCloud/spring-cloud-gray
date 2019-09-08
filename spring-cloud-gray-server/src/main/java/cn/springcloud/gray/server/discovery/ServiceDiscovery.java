package cn.springcloud.gray.server.discovery;

import cn.springcloud.gray.model.InstanceInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 从注册中心获取服务信息
 */
public interface ServiceDiscovery {

    List<ServiceInfo> listAllSerivceInfos();

    ServiceInfo getServiceInfo(String serviceId);

    List<InstanceInfo> listInstanceInfos(String serviceId);

    InstanceInfo getInstanceInfo(String serviceId, String instanceId);

    default Map<String, InstanceInfo> getInstanceInfos(String serviceId) {
        return listInstanceInfos(serviceId).stream().collect(Collectors.toMap(info -> info.getInstanceId(), info -> info));
    }
}
