package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.module.domain.InstanceStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleGrayModule implements GrayModle {

    private GrayServerModule grayServerModule;
    private ObjectMapper objectMapper;

    public SimpleGrayModule(GrayServerModule grayServerModule, ObjectMapper objectMapper) {
        this.grayServerModule = grayServerModule;
        this.objectMapper = objectMapper;
    }

    @Override
    public GrayServerModule getGrayServerModule() {
        return grayServerModule;
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        cn.springcloud.gray.server.module.domain.GrayInstance grayInstance = grayServerModule.getGrayInstance(instanceId);
        if (grayInstance == null) {
            return null;
        }
        return ofGrayInstance(grayInstance);
    }


    @Override
    public List<cn.springcloud.gray.model.GrayInstance> allOpenInstances() {
        List<cn.springcloud.gray.server.module.domain.GrayInstance> instances = grayServerModule.listGrayInstancesByStatus(GrayStatus.OPEN, InstanceStatus.UP);

        List<cn.springcloud.gray.model.GrayInstance> grayInstances = new ArrayList<>(instances.size());
        instances.forEach(instance -> {
            cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstance(instance);
            grayInstance.setPolicyDefinitions(ofGrayPoliciesByInstanceId(instance.getInstanceId()));
            grayInstances.add(grayInstance);
        });
        return grayInstances;
    }


    private cn.springcloud.gray.model.GrayInstance ofGrayInstance(cn.springcloud.gray.server.module.domain.GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = new cn.springcloud.gray.model.GrayInstance();
        grayInstance.setPort(instance.getPort());
        grayInstance.setServiceId(instance.getServiceId());
        grayInstance.setInstanceId(instance.getInstanceId());
        grayInstance.setGrayStatus(instance.getGrayStatus());
        grayInstance.setHost(instance.getHost());
        return grayInstance;
    }

    private List<PolicyDefinition> ofGrayPoliciesByInstanceId(String instanceId) {
        List<GrayPolicy> grayPolicies = grayServerModule.listGrayPoliciesByInstanceId(instanceId);
        List<PolicyDefinition> policyDefinitions = new ArrayList<>(grayPolicies.size());
        grayPolicies.forEach(grayPolicy -> {
            PolicyDefinition policyDefinition = ofGrayPolicy(grayPolicy);
            policyDefinition.setList(ofGrayDecisionByPolicyId(grayPolicy.getId()));
        });

        return policyDefinitions;
    }

    private PolicyDefinition ofGrayPolicy(GrayPolicy grayPolicy) {
        PolicyDefinition policyDefinition = new PolicyDefinition();
        policyDefinition.setAlias(grayPolicy.getAlias());
        policyDefinition.setPolicyId(String.valueOf(grayPolicy.getId()));
        return policyDefinition;
    }

    private List<DecisionDefinition> ofGrayDecisionByPolicyId(Long policyId) {
        List<GrayDecision> grayDecisions = grayServerModule.listGrayDecisionsByPolicyId(policyId);
        List<DecisionDefinition> decisionDefinitions = new ArrayList<>(grayDecisions.size());
        grayDecisions.forEach(grayDecision -> {
            try {
                decisionDefinitions.add(ofGrayDecision(grayDecision));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return decisionDefinitions;
    }


    private DecisionDefinition ofGrayDecision(GrayDecision grayDecision) throws IOException {
        DecisionDefinition decisionDefinition = new DecisionDefinition();
        decisionDefinition.setId(String.valueOf(grayDecision.getId()));
        decisionDefinition.setName(grayDecision.getName());
        decisionDefinition.setInfos(objectMapper.readValue(grayDecision.getInfos(), new TypeReference<Map<String, String>>() {
        }));
        return decisionDefinition;
    }
}
