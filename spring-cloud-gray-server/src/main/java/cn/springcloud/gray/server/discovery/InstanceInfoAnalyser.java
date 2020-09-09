package cn.springcloud.gray.server.discovery;

import cn.springcloud.gray.model.InstanceInfo;

/**
 * @author saleson
 * @date 2020-09-09 08:14
 */
public interface InstanceInfoAnalyser<INSTANCE> {

    void analyse(INSTANCE instance, InstanceInfo instanceInfo);

}
