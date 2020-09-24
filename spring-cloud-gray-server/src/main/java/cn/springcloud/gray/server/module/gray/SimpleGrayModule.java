package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.function.Consumer2;
import cn.springcloud.gray.model.*;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.constant.Version;
import cn.springcloud.gray.server.module.HandleModule;
import cn.springcloud.gray.server.module.HandleRuleModule;
import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.domain.query.HandleRuleQuery;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.server.module.route.policy.domain.query.RoutePolicyQuery;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SimpleGrayModule implements GrayModule {

    private GrayServerProperties grayServerProperties;
    private GrayPolicyModule grayPolicyModule;
    private GrayServerModule grayServerModule;
    private RoutePolicyModule routePolicyModule;
    private GrayServerTrackModule grayServerTrackModule;
    private ObjectMapper objectMapper;
    private GrayEventLogModule grayEventLogModule;
    private HandleModule handleModule;
    private HandleRuleModule handleRuleModule;


    private Map<String, Consumer2<ServiceRouteInfo, RoutePolicyRecord>> routePolicyRecordTransferSetConsumers = new HashMap<>();

    public SimpleGrayModule(
            GrayServerProperties grayServerProperties,
            GrayPolicyModule grayPolicyModule,
            GrayServerModule grayServerModule,
            RoutePolicyModule routePolicyModule,
            GrayServerTrackModule grayServerTrackModule,
            GrayEventLogModule grayEventLogModule,
            ObjectMapper objectMapper,
            HandleModule handleModule,
            HandleRuleModule handleRuleModule) {
        this.grayServerProperties = grayServerProperties;
        this.grayPolicyModule = grayPolicyModule;
        this.grayServerModule = grayServerModule;
        this.routePolicyModule = routePolicyModule;
        this.grayServerTrackModule = grayServerTrackModule;
        this.objectMapper = objectMapper;
        this.grayEventLogModule = grayEventLogModule;
        this.handleModule = handleModule;
        this.handleRuleModule = handleRuleModule;
        this.init();
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
    public List<GrayInstance> allOpenInstances(Collection<String> serviceIds, Version version) {
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
        Set<String> routePolicies = policyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());
        grayInstance.setRoutePolicies(routePolicies);
        return grayInstance;
    }


    @Override
    public cn.springcloud.gray.model.GrayInstance ofGrayInstance
            (cn.springcloud.gray.server.module.gray.domain.GrayInstance instance) {
        cn.springcloud.gray.model.GrayInstance grayInstance = new cn.springcloud.gray.model.GrayInstance();
        grayInstance.setPort(instance.getPort());
        grayInstance.setServiceId(instance.getServiceId());
        grayInstance.setAliases(instance.getAliases());
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
        RoutePolicyQuery query = RoutePolicyQuery.builder()
                .type(RoutePolicyType.INSTANCE_ROUTE.name())
                .resource(instanceId)
                .delFlag(DelFlag.UNDELETE)
                .build();
        List<RoutePolicyRecord> instanceRoutePolicies = routePolicyModule.findAllRoutePolicies(query);
        return instanceRoutePolicies.stream().map(RoutePolicyRecord::getPolicyId).collect(Collectors.toList());
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
        List<GrayDecision> grayDecisions = grayPolicyModule.listEnabledGrayDecisionsByPolicyId(policyId);
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

    @Override
    public List<ServiceRouteInfo> listAllGrayServiceRouteInfosExcludeSpecial(String serviceId) {
        List<RoutePolicyRecord> routePolicyRecords = findAllServiceRoutePolicyRecords(null);
        List<RoutePolicyRecord> filteredRoutePolicyRecords = routePolicyRecords.stream()
                .filter(routePolicyRecord -> !Objects.equals(routePolicyRecord.getModuleId(), serviceId))
                .collect(Collectors.toList());
        return transferGrayServiceRouteInfoList(filteredRoutePolicyRecords);
    }

    @Override
    public List<ServiceRouteInfo> listAllGrayServiceRouteInfos(String serviceId) {
        List<RoutePolicyRecord> routePolicyRecords = findAllServiceRoutePolicyRecords(serviceId);
        return transferGrayServiceRouteInfoList(routePolicyRecords);
    }

    @Override
    public List<ServiceRouteInfo> listAllGrayServiceRouteInfos() {
        List<RoutePolicyRecord> routePolicyRecords = findAllServiceRoutePolicyRecords(null);
        return transferGrayServiceRouteInfoList(routePolicyRecords);
    }

    @Override
    public HandleDefinition toHandleDefinition(Handle handle) {
        HandleDefinition handleDefinition = handle2Definition(handle);
        List<HandleAction> handleActions = handleModule.listEnabledHandleActionsByHandleId(handle.getId());
        Set<HandleActionDefinition> handleActionDefinitions = handleActions.stream()
                .map(this::toHandleActionDefinition)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        handleDefinition.setHandleActionDefinitions(handleActionDefinitions);
        return handleDefinition;
    }

    @Override
    public HandleActionDefinition toHandleActionDefinition(HandleAction handleAction) {
        HandleActionDefinition definition = new HandleActionDefinition();
        definition.setId(String.valueOf(handleAction.getId()));
        definition.setActionName(handleAction.getActionName());
        definition.setOrder(handleAction.getOrder());
        if (StringUtils.isEmpty(handleAction.getInfos())) {
            return null;
        }
        try {
            definition.setInfos(objectMapper.readValue(handleAction.getInfos(), new TypeReference<Map<String, String>>() {
            }));
        } catch (IOException e) {
            log.error("解析HandleAction.infos失败，{}", handleAction, e);
            return null;
        }
        return definition;
    }

    @Override
    public List<HandleDefinition> listAllEnabledHandles() {
        return handleModule.findAllEnabledHandles().stream()
                .map(this::toHandleDefinition)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public HandleRuleDefinition toHandleRuleDefinition(HandleRule handleRule) {
        HandleRuleDefinition definition = new HandleRuleDefinition();
        definition.setId(String.valueOf(handleRule.getId()));
        definition.setHandleOption(handleRule.getHandleOption());
        definition.setType(handleRule.getType());
        definition.setOrder(handleRule.getOrder());
        Set<String> matchingPolicyIds = new HashSet<>(handleRule.getMatchingPolicyIds().length);
        for (Long matchingPolicyId : handleRule.getMatchingPolicyIds()) {
            matchingPolicyIds.add(String.valueOf(matchingPolicyId));
        }
        definition.setMatchingPolicyIds(matchingPolicyIds);
        return definition;
    }

    @Override
    public List<HandleRuleDefinition> listAllEnabledHandleRules(String moduleId, String resource) {
        HandleRuleQuery handleRuleQuery = new HandleRuleQuery();
        handleRuleQuery.setDelFlag(DelFlag.UNDELETE);
        handleRuleQuery.setResource(resource);
        handleRuleQuery.setModuleId(moduleId);
        return handleRuleModule.findHandleRules(handleRuleQuery)
                .stream()
                .map(this::toHandleRuleDefinition)
                .collect(Collectors.toList());
    }

    protected void init() {
        initRoutePolicyRecordTransferSetConsumers();
    }

    protected List<ServiceRouteInfo> transferGrayServiceRouteInfoList(List<RoutePolicyRecord> routePolicyRecords) {
        Map<String, ServiceRouteInfo> serviceRouteInfoMap = transferGrayServiceRouteInfos(routePolicyRecords);
        return new ArrayList<>(serviceRouteInfoMap.values());
    }

    protected Map<String, ServiceRouteInfo> transferGrayServiceRouteInfos(List<RoutePolicyRecord> routePolicyRecords) {
        Map<String, ServiceRouteInfo> serviceRouteInfoMap = new HashMap<>();
        for (RoutePolicyRecord routePolicyRecord : routePolicyRecords) {
            String serviceId = routePolicyRecord.getModuleId();
            ServiceRouteInfo serviceRouteInfo = serviceRouteInfoMap.get(serviceId);
            if (Objects.isNull(serviceRouteInfo)) {
                serviceRouteInfo = ServiceRouteInfo.builder()
                        .serviceId(serviceId)
                        .routePolicies(new LinkedHashSet<>())
                        .multiVersionRoutePolicies(new HashMap<>())
                        .build();
                serviceRouteInfoMap.put(serviceId, serviceRouteInfo);
            }
            parseAndSetRoutePolicyRecord(routePolicyRecord, serviceRouteInfo);
        }
        return serviceRouteInfoMap;
    }


    private void initRoutePolicyRecordTransferSetConsumers() {
        routePolicyRecordTransferSetConsumers.put(RoutePolicyType.SERVICE_ROUTE.name(), (serviceRouteInfo, routePolicyRecord) -> {
            serviceRouteInfo.getRoutePolicies().add(String.valueOf(routePolicyRecord.getPolicyId()));
        });

        routePolicyRecordTransferSetConsumers.put(RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name(), (serviceRouteInfo, routePolicyRecord) -> {
            Set<String> versionRoutePolicies =
                    serviceRouteInfo.getMultiVersionRoutePolicies().get(routePolicyRecord.getResource());
            if (Objects.isNull(versionRoutePolicies)) {
                versionRoutePolicies = new LinkedHashSet<>();
                serviceRouteInfo.getMultiVersionRoutePolicies().put(routePolicyRecord.getResource(), versionRoutePolicies);
            }
            versionRoutePolicies.add(String.valueOf(routePolicyRecord.getPolicyId()));
        });
    }

    private void parseAndSetRoutePolicyRecord(RoutePolicyRecord routePolicyRecord, ServiceRouteInfo serviceRouteInfo) {
        Consumer2<ServiceRouteInfo, RoutePolicyRecord> consumer =
                routePolicyRecordTransferSetConsumers.get(routePolicyRecord.getType());
        if (Objects.isNull(consumer)) {
            log.warn("没有找到type为'{}'的Consumer2<GrayServiceRouteInfo, RoutePolicyRecord>", routePolicyRecord.getType());
            return;
        }
        consumer.accept(serviceRouteInfo, routePolicyRecord);
    }

    private List<RoutePolicyRecord> findAllServiceRoutePolicyRecords(String serviceId) {
        RoutePolicyQuery query = RoutePolicyQuery.builder()
                .delFlag(DelFlag.UNDELETE)
                .type(RoutePolicyType.SERVICE_ROUTE.name())
                .build();
        if (StringUtils.isNotEmpty(serviceId)) {
            query.setModuleId(serviceId);
        }
        List<RoutePolicyRecord> serviceRoutePolicyRecords = routePolicyModule.findAllRoutePolicies(query);
        query.setType(RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name());
        List<RoutePolicyRecord> versionRoutePolicyRecords = routePolicyModule.findAllRoutePolicies(query);
        return ListUtils.union(serviceRoutePolicyRecords, versionRoutePolicyRecords);
    }


    private HandleDefinition handle2Definition(Handle handle) {
        HandleDefinition handleDefinition = new HandleDefinition();
        handleDefinition.setType(handle.getType());
        handleDefinition.setName(handle.getName());
        handleDefinition.setId(String.valueOf(handle.getId()));
        return handleDefinition;
    }

}
