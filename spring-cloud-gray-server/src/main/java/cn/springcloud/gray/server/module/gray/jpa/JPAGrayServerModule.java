package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GraySourceEventPublisher;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import cn.springcloud.gray.server.service.GrayServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JPAGrayServerModule implements GrayServerModule {

    private static final List<SourceType> INSTANCE_STATUS_CHECK_SOURCE_TYPES =
            Arrays.asList(SourceType.GRAY_POLICY, SourceType.GRAY_DECISION);

    private GrayServiceService grayServiceService;
    private GrayInstanceService grayInstanceService;
    private GrayDecisionService grayDecisionService;
    private GrayPolicyService grayPolicyService;
    private GraySourceEventPublisher grayEventPublisher;
    private GrayServerProperties grayServerProperties;
    private ServiceDiscovery serviceDiscovery;
    private ServiceManageModule serviceManageModule;

    public JPAGrayServerModule(
            GrayServerProperties grayServerProperties,
            GraySourceEventPublisher grayEventPublisher,
            ServiceDiscovery serviceDiscovery,
            GrayServiceService grayServiceService,
            GrayInstanceService grayInstanceService,
            GrayDecisionService grayDecisionService,
            GrayPolicyService grayPolicyService,
            ServiceManageModule serviceManageModule) {
        this.grayServerProperties = grayServerProperties;
        this.grayEventPublisher = grayEventPublisher;
        this.serviceDiscovery = serviceDiscovery;
        this.grayServiceService = grayServiceService;
        this.grayInstanceService = grayInstanceService;
        this.grayDecisionService = grayDecisionService;
        this.grayPolicyService = grayPolicyService;
        this.serviceManageModule = serviceManageModule;
    }

    @Override
    public List<GrayService> allGrayServices() {
        return grayServiceService.findAllModel();
    }

    @Override
    public List<GrayInstance> listGrayInstancesByStatus(GrayStatus grayStatus, Collection<InstanceStatus> instanceStatus) {
        return grayInstanceService.findAllByStatus(grayStatus, instanceStatus);
    }


    @Override
    public List<GrayInstance> listGrayInstancesByNormalInstanceStatus(Collection<InstanceStatus> instanceStatus) {
        return grayInstanceService.listGrayInstancesByNormalInstanceStatus(instanceStatus);
    }


    @Transactional
    @Override
    public void deleteGrayService(String serviceId) {
        List<GrayInstance> grayInstances = grayInstanceService.findByServiceId(serviceId);
        grayServiceService.deleteReactById(serviceId);
        serviceManageModule.deleteSericeManeges(serviceId);
//        grayInstances.forEach(this::publishDownIntanceEvent);
        publishEventMsg(
                createEventMsg(SourceType.GRAY_SERVICE, EventType.DOWN, serviceId), null);
    }


    @Override
    public void updateGrayStatus(String instanceId, GrayStatus grayStatus) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (instance != null && !Objects.equals(instance.getGrayStatus(), grayStatus)) {
            instance.setGrayStatus(grayStatus);
            grayInstanceService.saveModel(instance);
            if (grayStatus == GrayStatus.OPEN) {
                publishUpdateIntanceEvent(instance);
            } else {
                publishDownIntanceEvent(instance);
            }
        }
    }


    @Transactional
    @Override
    public GrayInstance saveGrayInstance(GrayInstance instance) {
        GrayService grayService = grayServiceService.findOneModel(instance.getServiceId());
        if (grayService == null) {
            grayService = GrayService.builder()
                    .serviceId(instance.getServiceId())
                    .serviceName(instance.getServiceId())
                    .build();
            grayServiceService.saveModel(grayService);
            serviceManageModule.insertServiceOwner(grayService.getServiceId());
        }
        if (Objects.isNull(instance.getInstanceStatus()) && !Objects.isNull(serviceDiscovery)) {
            InstanceInfo instanceInfo =
                    serviceDiscovery.getInstanceInfo(instance.getServiceId(), instance.getInstanceId());
            if (!Objects.isNull(instanceInfo)) {
                instance.setInstanceStatus(instanceInfo.getInstanceStatus());
            }
        }

        GrayInstance oldRecord = grayInstanceService.findOneModel(instance.getInstanceId());
        GrayInstance newRecord = grayInstanceService.saveModel(instance);
        if (isLockGray(newRecord) ||
                grayServerProperties.getInstance().getNormalInstanceStatus().contains(instance.getInstanceStatus())) {
            if (Objects.equals(newRecord.getGrayStatus(), GrayStatus.OPEN)) {
                publishUpdateIntanceEvent(newRecord);
            } else if (oldRecord != null && Objects.equals(oldRecord.getGrayStatus(), GrayStatus.OPEN)) {
                publishDownIntanceEvent(newRecord);
            }
        }
        return newRecord;
    }

    @Override
    public void updateInstanceStatus(String instanceId, InstanceStatus instanceStatus) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        updateInstanceStatus(instance, instanceStatus);
    }

    @Override
    public void updateInstanceStatus(GrayInstance instance, InstanceStatus instanceStatus) {
        if (instance != null && !Objects.equals(instance.getInstanceStatus(), instanceStatus)) {
            instance.setInstanceStatus(instanceStatus);
            grayInstanceService.saveModel(instance);

            if (isLockGray(instance)) {
                return;
            }
            if (instance.getGrayStatus() == GrayStatus.OPEN) {
                if (grayServerProperties.getInstance().getNormalInstanceStatus().contains(instanceStatus)) {
                    publishUpdateIntanceEvent(instance);
                } else {
                    publishDownIntanceEvent(instance);
                }
            }
        }
    }

    @Transactional
    @Override
    public void deleteGrayInstance(String intanceId) {
        GrayInstance grayInstance = grayInstanceService.findOneModel(intanceId);
        if (grayInstance != null) {
            grayInstanceService.deleteReactById(intanceId);
            publishDownIntanceEvent(grayInstance);
        }
    }

    private boolean isLockGray(GrayInstance instance) {
        return Objects.equals(instance.getGrayLock(), GrayInstance.GRAY_LOCKED);
    }

    @Override
    public GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy) {
        GrayPolicy newRecord = grayPolicyService.saveModel(grayPolicy);
//        publishUpdateIntanceEvent(grayInstanceService.findOneModel(grayPolicy.getInstanceId()));
        publishEventMsg(
                createEventMsg(SourceType.GRAY_POLICY, EventType.UPDATE,
                        grayInstanceService.findOneModel(newRecord.getInstanceId())), newRecord);
        return newRecord;
    }

    @Transactional
    @Override
    public void deleteGrayPolicy(Long policyId) {
        GrayPolicy policy = grayPolicyService.findOneModel(policyId);
        if (policy != null) {
            grayPolicyService.deleteReactById(policyId);
//            publishUpdateIntanceEvent(grayInstanceService.findOneModel(policy.getInstanceId()));
            publishEventMsg(
                    createEventMsg(SourceType.GRAY_POLICY, EventType.DOWN,
                            grayInstanceService.findOneModel(policy.getInstanceId())), policy);
        }
    }

    @Override
    public GrayDecision saveGrayDecision(GrayDecision grayDecision) {
        GrayPolicy policy = grayPolicyService.findOneModel(grayDecision.getPolicyId());
        grayDecision.setInstanceId(policy.getInstanceId());
        GrayDecision newRecord = grayDecisionService.saveModel(grayDecision);
//        publishUpdateIntanceEvent(grayInstanceService.findOneModel(grayDecision.getInstanceId()));

        publishEventMsg(
                createEventMsg(SourceType.GRAY_DECISION, EventType.UPDATE,
                        grayInstanceService.findOneModel(newRecord.getInstanceId())), newRecord);
        return newRecord;
    }

    @Override
    public void deleteGrayDecision(Long decisionId) {
        GrayDecision decision = grayDecisionService.findOneModel(decisionId);
        if (decision != null) {
            grayDecisionService.delete(decisionId);
            publishEventMsg(
                    createEventMsg(SourceType.GRAY_DECISION, EventType.DOWN,
                            grayInstanceService.findOneModel(decision.getInstanceId())), decision);
//            publishUpdateIntanceEvent(grayInstanceService.findOneModel(decision.getInstanceId()));
        }

    }


    @Override
    public GrayDecision getGrayDecision(Long id) {
        return grayDecisionService.findOneModel(id);
    }

    @Override
    public List<GrayDecision> listGrayDecisionsByPolicyId(Long policyId) {
        return grayDecisionService.findByPolicyId(policyId);
    }

    @Override
    public List<GrayInstance> listGrayInstancesByServiceId(String serviceId) {
        return grayInstanceService.findByServiceId(serviceId);
    }

    @Override
    public List<GrayInstance> listGrayInstancesByServiceId(String serviceId, Collection<InstanceStatus> instanceStatus) {
        return grayInstanceService.findByServiceId(serviceId, instanceStatus);
    }

    @Override
    public GrayInstance getGrayInstance(String id) {
        return grayInstanceService.findOneModel(id);
    }

    @Override
    public List<GrayService> listAllGrayServices() {
        return grayServiceService.findAllModel();
    }

    @Transactional
    @Override
    public GrayService saveGrayService(GrayService grayService) {
        GrayService record = grayServiceService.saveModel(grayService);
        if (serviceManageModule.getServiceOwner(grayService.getServiceId()) == null) {
            serviceManageModule.addServiceOwner(grayService.getServiceId());
        }
        publishEventMsg(
                createEventMsg(SourceType.GRAY_SERVICE, EventType.UPDATE, record.getServiceId()), record);
        return record;
    }

    @Override
    public GrayService getGrayService(String id) {
        return grayServiceService.findOneModel(id);
    }

    @Override
    public List<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId) {
        return grayPolicyService.findByInstanceId(instanceId);
    }

    @Override
    public Page<GrayService> listAllGrayServices(Pageable pageable) {
        return grayServiceService.listAllGrayServices(pageable);
    }

    @Override
    public List<GrayService> findGrayServices(Iterable<String> serviceIds) {
        return grayServiceService.findAllModel(serviceIds);
    }

    @Override
    public Page<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId, Pageable pageable) {
        return grayPolicyService.listGrayPoliciesByInstanceId(instanceId, pageable);
    }

    @Override
    public Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable) {
        return grayInstanceService.listGrayInstancesByServiceId(serviceId, pageable);
    }

    @Override
    public Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable) {
        return grayDecisionService.listGrayDecisionsByPolicyId(policyId, pageable);
    }

    protected GraySourceEventPublisher getGrayEventPublisher() {
        return grayEventPublisher;
    }


    private void publishUpdateIntanceEvent(GrayInstance grayInstance) {
        GrayEventMsg eventMsg = createEventMsg(SourceType.GRAY_INSTANCE, EventType.UPDATE, grayInstance);
        if (eventMsg != null) {
            log.info("push event message -> {}", eventMsg);
            getGrayEventPublisher().publishEvent(eventMsg, grayInstance, 100);
        }
    }


    private GrayEventMsg createEventMsg(SourceType sourceType, EventType eventType, GrayInstance grayInstance) {
        if (!isNeesPushEentMsg(sourceType, grayInstance)) {
            return null;
        }
        return GrayEventMsg.builder()
                .serviceId(grayInstance.getServiceId())
                .instanceId(grayInstance.getInstanceId())
                .eventType(eventType)
                .sourceType(sourceType)
                .build();
    }

    private boolean isNeesPushEentMsg(SourceType sourceType, GrayInstance grayInstance) {
        if (INSTANCE_STATUS_CHECK_SOURCE_TYPES.contains(sourceType)) {
            return Objects.equals(grayInstance.getGrayStatus(), GrayStatus.OPEN)
                    && (
                    isLockGray(grayInstance) ||
                            grayServerProperties.getInstance().getNormalInstanceStatus().contains(grayInstance.getInstanceStatus()));
        }
        return true;
    }

    private GrayEventMsg createEventMsg(SourceType sourceType, EventType eventType, String serviceId) {
        return GrayEventMsg.builder()
                .serviceId(serviceId)
                .eventType(eventType)
                .sourceType(sourceType)
                .build();
    }


    private void publishDownIntanceEvent(GrayInstance grayInstance) {
        publishEventMsg(
                createEventMsg(SourceType.GRAY_INSTANCE, EventType.DOWN, grayInstance), grayInstance);
    }

    public void publishEventMsg(GrayEventMsg eventMsg, Object source) {
        if (eventMsg == null) {
            return;
        }
        log.info("push event message -> {}", eventMsg);
        getGrayEventPublisher().asyncPublishEvent(eventMsg, source);
    }

}
