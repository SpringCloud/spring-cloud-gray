package cn.springcloud.gray.servernode;

import cn.springcloud.gray.model.InstanceStatus;

public interface InstanceDiscoveryClient {


    void setStatus(InstanceStatus status);
}
