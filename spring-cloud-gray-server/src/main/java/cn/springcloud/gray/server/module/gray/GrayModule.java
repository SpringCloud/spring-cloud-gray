package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;

import java.io.IOException;
import java.util.List;

public interface GrayModule {

    GrayServerModule getGrayServerModule();

    GrayTrackDefinition ofGrayTrack(GrayTrack grayTrack);

    List<GrayInstance> allOpenInstances();

    GrayInstance getGrayInstance(String serviceId, String instanceId);


    List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId);

    GrayInstance ofGrayInstance(cn.springcloud.gray.server.module.gray.domain.GrayInstance instance);

    PolicyDefinition ofGrayPolicy(GrayPolicy grayPolicy);

    DecisionDefinition ofGrayDecision(GrayDecision grayDecision) throws IOException;
}
