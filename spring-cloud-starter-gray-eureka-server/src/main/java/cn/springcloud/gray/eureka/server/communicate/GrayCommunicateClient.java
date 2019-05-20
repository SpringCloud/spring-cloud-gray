package cn.springcloud.gray.eureka.server.communicate;

import cn.springcloud.gray.model.InstanceInfo;

public interface GrayCommunicateClient {


    /**
     * 将实例信息发送到灰度服务器
     *
     * @param instanceInfo 实例信息
     */
    void noticeInstanceInfo(InstanceInfo instanceInfo);

}
