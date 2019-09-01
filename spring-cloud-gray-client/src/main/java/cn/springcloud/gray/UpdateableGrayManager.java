package cn.springcloud.gray;

import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.PolicyDefinition;

import java.util.Collection;

public interface UpdateableGrayManager extends GrayManager {

    void setGrayServices(Object grayServices);

    void setRequestInterceptors(Collection<RequestInterceptor> requestInterceptors);

    void removeGrayService(String serviceId);

    void removePolicyDefinition(String serviceId, String instanceId, String policyId);

    void updatePolicyDefinition(String serviceId, String instanceId, PolicyDefinition policyDefinition);

    void removeDecisionDefinition(String serviceId, String instanceId, String policyId, String decisionId);

    void updateDecisionDefinition(String serviceId, String instanceId, String policyId, DecisionDefinition decisionDefinition);

}
