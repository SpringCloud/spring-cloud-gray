package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.server.module.domain.*;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import cn.springcloud.gray.server.service.GrayServiceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SimpleGrayModule implements GrayModule {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GrayServiceService grayServiceService;
    @Autowired
    private GrayInstanceService grayInstanceService;
    @Autowired
    private GrayDecisionService grayDecisionService;
    @Autowired
    private GrayPolicyService grayPolicyService;

    @Override
    public List<GrayService> allGrayService() {
        return grayServiceService.findAllModel();
    }

    @Override
    public List<GrayInstance> listGrayInstanceBySerivceId(String serviceId) {
        return grayInstanceService.findByServiceId(serviceId);
    }

    @Override
    public void deleteGrayService(String serviceId) {
        grayServiceService.deleteById(serviceId);
    }


    @Override
    public void updateGrayStatus(String instanceId, GrayStatus grayStatus) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (instance != null) {
            instance.setGrayStatus(grayStatus);
            grayInstanceService.saveModel(instance);
        }
    }

    @Override
    public void addGrayService(GrayService service) {
        grayServiceService.saveModel(service);
    }

    @Override
    public void addGrayInstance(GrayInstance instance) {
        grayInstanceService.saveModel(instance);
    }

    @Override
    public void updateGrayInstance(GrayInstance instance) {
        grayInstanceService.saveModel(instance);
    }

    @Override
    public void updateInstanceStatus(String instanceId, InstanceStatus instanceStatus) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (instance != null) {
            instance.setInstanceStatus(instanceStatus);
            grayInstanceService.saveModel(instance);
        }
    }

    @Override
    public void deleteGrayInstance(String intanceId) {
        grayInstanceService.deleteReactById(intanceId);
    }

    @Override
    public void addGrayPolicy(GrayPolicy grayPolicy) {
        grayPolicyService.saveModel(grayPolicy);
    }

    @Override
    public void updateGrayPolicy(GrayPolicy grayPolicy) {
        grayPolicyService.saveModel(grayPolicy);
    }

    @Override
    public void deleteGrayPolicy(Long policyId) {
        grayPolicyService.deleteReactById(policyId);
    }

    @Override
    public void addGrayDecision(GrayDecision grayDecision) {
        grayDecisionService.saveModel(grayDecision);
    }

    @Override
    public void updateGrayDecision(GrayDecision grayDecision) {
        grayDecisionService.saveModel(grayDecision);
    }

    @Override
    public void deleteGrayDecision(Long decisionId) {
        grayDecisionService.delete(decisionId);
    }

    @Override
    public List<cn.springcloud.gray.model.GrayInstance> allOpenInstances() {
        List<GrayInstance> instances = grayInstanceService.findAllByGrayStatus(GrayStatus.OPEN);

        List<cn.springcloud.gray.model.GrayInstance> grayInstances = new ArrayList<>(instances.size());
        instances.forEach(instance -> {
            cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstance(instance);
            grayInstance.setPolicyDefinitions(ofGrayPoliciesByInstanceId(instance.getInstanceId()));
            grayInstances.add(grayInstance);
        });
        return grayInstances;
    }

    private cn.springcloud.gray.model.GrayInstance ofGrayInstance(GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = new cn.springcloud.gray.model.GrayInstance();
        grayInstance.setPort(instance.getPort());
        grayInstance.setServiceId(instance.getServiceId());
        grayInstance.setInstanceId(instance.getInstanceId());
        grayInstance.setGrayStatus(instance.getGrayStatus());
        grayInstance.setHost(instance.getHost());
        return grayInstance;
    }

    private List<PolicyDefinition> ofGrayPoliciesByInstanceId(String instanceId) {
        List<GrayPolicy> grayPolicies = grayPolicyService.findByInstanceId(instanceId);
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
        List<GrayDecision> grayDecisions = grayDecisionService.findByPolicyId(policyId);
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
