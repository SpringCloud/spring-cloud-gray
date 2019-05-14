package cn.springcloud.gray.server.discovery;

import java.util.List;


/**
 * 从注册中心获取服务信息
 */
public interface ServiceDiscover {

    List<ServiceInfo> listAllSerivceInfos();

    ServiceInfo getServiceInfo(String serviceId);

    List<InstanceInfo> listInstanceInfos(String serviceId);

    InstanceInfo getInstanceInfo(String serviceId, String instanceId);

}
