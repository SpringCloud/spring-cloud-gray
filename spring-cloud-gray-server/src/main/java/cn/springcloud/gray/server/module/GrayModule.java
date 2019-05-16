package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;

import java.util.List;

public interface GrayModule {

    GrayServerModule getGrayServerModule();

    List<GrayInstance> allOpenInstances();

    GrayInstance getGrayInstance(String serviceId, String instanceId);


    List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId);
}
