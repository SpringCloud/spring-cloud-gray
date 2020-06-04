package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.model.*;
import cn.springcloud.gray.server.constant.Version;
import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public interface GrayModule {

    GrayServerModule getGrayServerModule();

    GrayTrackDefinition ofGrayTrack(GrayTrack grayTrack);

    List<GrayInstance> allOpenInstances(Version version);

    List<GrayInstance> allOpenInstances(Iterator<String> serviceIds, Version version);

    default GrayInstance getGrayInstance(String serviceId, String instanceId) {
        return getGrayInstance(serviceId, instanceId, Version.V2);
    }

    GrayInstance getGrayInstance(String serviceId, String instanceId, Version version);


    List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId);

    GrayInstance ofGrayInstance(cn.springcloud.gray.server.module.gray.domain.GrayInstance instance);

    @Deprecated
    List<PolicyDefinition> ofGrayPoliciesByInstanceId(String instanceId);

    List<PolicyDefinition> allGrayPolicies();

    List<Long> listPolicyIdsByInstanceId(String instanceId);

    PolicyDefinition ofGrayPolicyInfo(GrayPolicy grayPolicy);

    PolicyDefinition ofGrayPolicy(GrayPolicy grayPolicy);

    List<DecisionDefinition> ofGrayDecisionByPolicyId(Long policyId);

    DecisionDefinition ofGrayDecision(GrayDecision grayDecision) throws IOException;

    long getMaxSortMark();

    List<ServiceRouteInfo> listAllGrayServiceRouteInfosExcludeSpecial(String serviceId);

    List<ServiceRouteInfo> listAllGrayServiceRouteInfos(String serviceId);

    List<ServiceRouteInfo> listAllGrayServiceRouteInfos();

    HandleDefinition toHandleDefinition(Handle handle);

    HandleActionDefinition toHandleActionDefinition(HandleAction handleAction);

    List<HandleDefinition> listAllEnabledHandles();

    HandleRuleDefinition toHandleRuleDefinition(HandleRule handleRule);

    List<HandleRuleDefinition> listAllEnabledHandleRules(String moduleId, String resource);


}
