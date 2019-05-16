package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.*;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import cn.springcloud.gray.server.module.domain.InstanceStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleGrayModule implements GrayModule {

    private GrayServerModule grayServerModule;
    private GrayServerTrackModule grayServerTrackModule;
    private ObjectMapper objectMapper;

    public SimpleGrayModule(
            GrayServerModule grayServerModule,
            GrayServerTrackModule grayServerTrackModule,
            ObjectMapper objectMapper) {
        this.grayServerModule = grayServerModule;
        this.grayServerTrackModule = grayServerTrackModule;
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
        return ofGrayInstanceInfo(grayInstance);
    }

    @Override
    public List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId) {
        List<GrayTrack> grayTracks = grayServerTrackModule.listGrayTracksEmptyInstanceByServiceId(serviceId);
        List<GrayTrackDefinition> trackDefinitions = new ArrayList<>(grayTracks.size());
        addGrayTrackDefinitions(trackDefinitions, grayTracks);
        addGrayTrackDefinitions(trackDefinitions, grayServerTrackModule.listGrayTracksByInstanceId(instanceId));
        return trackDefinitions;
    }

    private void addGrayTrackDefinitions(List<GrayTrackDefinition> trackDefinitions, List<GrayTrack> grayTracks) {
        grayTracks.forEach(track -> {
            trackDefinitions.add(ofGrayTrack(track));
        });
    }


    private GrayTrackDefinition ofGrayTrack(GrayTrack grayTrack) {
        GrayTrackDefinition definition = new GrayTrackDefinition();
        definition.setName(grayTrack.getName());
        definition.setValue(grayTrack.getInfos());
        return definition;
    }


    @Override
    public List<cn.springcloud.gray.model.GrayInstance> allOpenInstances() {
        List<cn.springcloud.gray.server.module.domain.GrayInstance> instances = grayServerModule.listGrayInstancesByStatus(GrayStatus.OPEN, InstanceStatus.UP);

        List<cn.springcloud.gray.model.GrayInstance> grayInstances = new ArrayList<>(instances.size());
        instances.forEach(instance -> {
            cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstanceInfo(instance);
            grayInstances.add(grayInstance);
        });
        return grayInstances;
    }

    private cn.springcloud.gray.model.GrayInstance ofGrayInstanceInfo(cn.springcloud.gray.server.module.domain.GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstance(instance);
        grayInstance.setPolicyDefinitions(ofGrayPoliciesByInstanceId(instance.getInstanceId()));
        return grayInstance;
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
            policyDefinitions.add(policyDefinition);
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
                DecisionDefinition definition = ofGrayDecision(grayDecision);
                if (definition != null) {
                    decisionDefinitions.add(definition);
                }
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
        if (StringUtils.isEmpty(grayDecision.getInfos())) {
            return null;
        }
        decisionDefinition.setInfos(objectMapper.readValue(grayDecision.getInfos(), new TypeReference<Map<String, String>>() {
        }));
        return decisionDefinition;
    }
}
