package cn.springcloud.gray.eureka.server.communicate;

import cn.springcloud.gray.model.InstanceInfo;

public interface GrayCommunicateClient {


    void noticeInstanceInfo(InstanceInfo instanceInfo);

}
