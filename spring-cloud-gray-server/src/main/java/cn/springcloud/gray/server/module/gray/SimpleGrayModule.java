package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.model.*;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.constant.Version;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SimpleGrayModule implements GrayModule {

    private GrayServerProperties grayServerProperties;
    private GrayPolicyModule grayPolicyModule;
    private GrayServerModule grayServerModule;
    private InstanceRouteModule instanceRouteModule;
    private GrayServerTrackModule grayServerTrackModule;
    private ObjectMapper objectMapper;
    private GrayEventLogModule grayEventLogModule;

    public SimpleGrayModule(
            GrayServerProperties grayServerProperties,
            GrayPolicyModule grayPolicyModule,
            GrayServerModule grayServerModule,
            InstanceRouteModule instanceRouteModule,
            GrayServerTrackModule grayServerTrackModule,
            GrayEventLogModule grayEventLogModule,
            ObjectMapper objectMapper) {
        this.grayServerProperties = grayServerProperties;
        this.grayPolicyModule = grayPolicyModule;
        this.grayServerModule = grayServerModule;
        this.instanceRouteModule = instanceRouteModule;
        this.grayServerTrackModule = grayServerTrackModule;
        this.objectMapper = objectMapper;
        this.grayEventLogModule = grayEventLogModule;
    }

    @Override
    public GrayServerModule getGrayServerModule() {
        return grayServerModule;
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId, Version version) {
        cn.springcloud.gray.server.module.gray.domain.GrayInstance grayInstance = grayServerModule.getGrayInstance(instanceId);
        if (grayInstance == null) {
            return null;
        }

        return ofGrayInstanceInfo(grayInstance, version);
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

    @Override
    public GrayTrackDefinition ofGrayTrack(GrayTrack grayTrack) {
        GrayTrackDefinition definition = new GrayTrackDefinition();
        definition.setName(grayTrack.getName());
        definition.setValue(grayTrack.getInfos());
        return definition;
    }


    @Override
    public List<cn.springcloud.gray.model.GrayInstance> allOpenInstances(Version version) {
        List<cn.springcloud.gray.server.module.gray.domain.GrayInstance> instances =
                grayServerModule.listGrayInstancesByNormalInstanceStatus(
                        grayServerProperties.getInstance().getNormalInstanceStatus());
        return ofallOpenInstances(instances, version);
    }

    @Override
    public List<GrayInstance> allOpenInstances(Iterator<String> serviceIds, Version version) {
        List<cn.springcloud.gray.server.module.gray.domain.GrayInstance> instances =
                grayServerModule.listGrayInstances(
                        serviceIds, grayServerProperties.getInstance().getNormalInstanceStatus());
        return ofallOpenInstances(instances, version);
    }

    private List<cn.springcloud.gray.model.GrayInstance> ofallOpenInstances(
            List<cn.springcloud.gray.server.module.gray.domain.GrayInstance> instances, Version version) {
        List<cn.springcloud.gray.model.GrayInstance> grayInstances = new ArrayList<>(instances.size());
        instances.forEach(instance -> {
            if (Objects.equals(instance.getGrayStatus(), GrayStatus.CLOSE)) {
                return;
            }
            cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstanceInfo(instance, version);
            grayInstances.add(grayInstance);
        });
        return grayInstances;
    }


    private cn.springcloud.gray.model.GrayInstance ofOldGrayInstanceInfo(cn.springcloud.gray.server.module.gray.domain.GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstance(instance);
        if (grayInstance.isGray()) {
            grayInstance.setPolicyDefinitions(ofGrayPoliciesByInstanceId(instance.getInstanceId()));
        }
        return grayInstance;
    }

    private cn.springcloud.gray.model.GrayInstance ofGrayInstanceInfo(
            cn.springcloud.gray.server.module.gray.domain.GrayInstance instance, Version version) {
        if (Objects.equals(version, Version.V1)) {
            return ofOldGrayInstanceInfo(instance);
        }
        cn.springcloud.gray.model.GrayInstance grayInstance = ofGrayInstance(instance);
        List<Long> policyIds = listPolicyIdsByInstanceId(grayInstance.getInstanceId());
        grayInstance.setRoutePolicies(new HashSet<>(policyIds.stream().map(String::valueOf).collect(Collectors.toList())));
        return grayInstance;
    }


    @Override
    public cn.springcloud.gray.model.GrayInstance ofGrayInstance
            (cn.springcloud.gray.server.module.gray.domain.GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = new cn.springcloud.gray.model.GrayInstance();
        grayInstance.setPort(instance.getPort());
        grayInstance.setServiceId(instance.getServiceId());
        grayInstance.setInstanceId(instance.getInstanceId());
        grayInstance.setGrayStatus(instance.getGrayStatus());
        grayInstance.setHost(instance.getHost());
        return grayInstance;
    }

    @Override
    public List<PolicyDefinition> ofGrayPoliciesByInstanceId(String instanceId) {
        List<Long> policyIds = listPolicyIdsByInstanceId(instanceId);
        List<GrayPolicy> grayPolicies = grayPolicyModule.findAllGrayPolicies(policyIds, false);
        List<PolicyDefinition> policyDefinitions = new ArrayList<>(grayPolicies.size());
        grayPolicies.forEach(grayPolicy -> {
            PolicyDefinition policyDefinition = ofGrayPolicyInfo(grayPolicy);
            policyDefinitions.add(policyDefinition);
        });

        return policyDefinitions;
    }

    @Override
    public List<PolicyDefinition> allGrayPolicies() {
        return grayPolicyModule.listAllEnabledGrayPolicies()
                .stream()
                .map(this::ofGrayPolicyInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> listPolicyIdsByInstanceId(String instanceId) {
        List<InstanceRoutePolicy> instanceRoutePolicies = instanceRouteModule.findAllRoutePoliciesByInstanceId(instanceId, false);
        return instanceRoutePolicies.stream().map(InstanceRoutePolicy::getPolicyId).collect(Collectors.toList());
    }


    @Override
    public PolicyDefinition ofGrayPolicyInfo(GrayPolicy grayPolicy) {
        PolicyDefinition policyDefinition = ofGrayPolicy(grayPolicy);
        policyDefinition.setList(ofGrayDecisionByPolicyId(grayPolicy.getId()));
        return policyDefinition;
    }

    @Override
    public PolicyDefinition ofGrayPolicy(GrayPolicy grayPolicy) {
        PolicyDefinition policyDefinition = new PolicyDefinition();
        policyDefinition.setAlias(grayPolicy.getAlias());
        policyDefinition.setPolicyId(String.valueOf(grayPolicy.getId()));
        return policyDefinition;
    }

    @Override
    public List<DecisionDefinition> ofGrayDecisionByPolicyId(Long policyId) {
        List<GrayDecision> grayDecisions = grayPolicyModule.listGrayDecisionsByPolicyId(policyId);
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


    @Override
    public DecisionDefinition ofGrayDecision(GrayDecision grayDecision) throws IOException {
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

    @Override
    public long getMaxSortMark() {
        return grayEventLogModule.getNewestSortMark();
    }
}
