package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.GrayInstance;

import java.util.List;

public interface GrayModle {

    GrayServerModule getGrayServerModule();

    List<GrayInstance> allOpenInstances();

    GrayInstance getGrayInstance(String serviceId, String instanceId);
}
