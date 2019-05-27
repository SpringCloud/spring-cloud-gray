package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import cn.springcloud.gray.server.module.domain.GrayService;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import cn.springcloud.gray.server.service.GrayServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JPAGrayServerModule implements GrayServerModule {

    private GrayServiceService grayServiceService;
    private GrayInstanceService grayInstanceService;
    private GrayDecisionService grayDecisionService;
    private GrayPolicyService grayPolicyService;
    private GrayEventPublisher grayEventPublisher;
    private GrayServerProperties grayServerProperties;
    private ServiceDiscovery serviceDiscovery;

    public JPAGrayServerModule(
            GrayServerProperties grayServerProperties,
            GrayEventPublisher grayEventPublisher,
            ServiceDiscovery serviceDiscovery,
            GrayServiceService grayServiceService,
            GrayInstanceService grayInstanceService,
            GrayDecisionService grayDecisionService,
            GrayPolicyService grayPolicyService) {
        this.grayServerProperties = grayServerProperties;
        this.grayEventPublisher = grayEventPublisher;
        this.serviceDiscovery = serviceDiscovery;
        this.grayServiceService = grayServiceService;
        this.grayInstanceService = grayInstanceService;
        this.grayDecisionService = grayDecisionService;
        this.grayPolicyService = grayPolicyService;
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


    @Override
    public void deleteGrayService(String serviceId) {
        List<GrayInstance> grayInstances = grayInstanceService.findByServiceId(serviceId);
        grayServiceService.deleteReactById(serviceId);
        grayInstances.forEach(this::publishDownIntanceEvent);
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


    @Override
    public void saveGrayInstance(GrayInstance instance) {
        GrayService grayService = grayServiceService.findOneModel(instance.getServiceId());
        if (grayService == null) {
            grayService = GrayService.builder()
                    .serviceId(instance.getServiceId())
                    .serviceName(instance.getServiceId())
                    .build();
            grayServiceService.saveModel(grayService);
        }
        if (Objects.isNull(instance.getInstanceStatus()) && !Objects.isNull(serviceDiscovery)) {
            InstanceInfo instanceInfo =
                    serviceDiscovery.getInstanceInfo(instance.getServiceId(), instance.getInstanceId());
            if (!Objects.isNull(instanceInfo)) {
                instance.setInstanceStatus(instanceInfo.getInstanceStatus());
            }

        }
        grayInstanceService.saveModel(instance);
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
            if (instance.getGrayStatus() == GrayStatus.OPEN) {
                if (grayServerProperties.getInstance().getNormalInstanceStatus().contains(instanceStatus)) {
                    publishUpdateIntanceEvent(instance);
                } else {
                    publishDownIntanceEvent(instance);
                }
            }
        }
    }

    @Override
    public void deleteGrayInstance(String intanceId) {
        GrayInstance grayInstance = grayInstanceService.findOneModel(intanceId);
        if (grayInstance != null) {
            grayInstanceService.deleteReactById(intanceId);
            publishDownIntanceEvent(grayInstance);
        }
    }

    @Override
    public void saveGrayPolicy(GrayPolicy grayPolicy) {
        grayPolicyService.saveModel(grayPolicy);
        publishUpdateIntanceEvent(grayInstanceService.findOneModel(grayPolicy.getInstanceId()));
    }

    @Override
    public void deleteGrayPolicy(Long policyId) {
        GrayPolicy policy = grayPolicyService.findOneModel(policyId);
        if (policy != null) {
            grayPolicyService.deleteReactById(policyId);
            publishUpdateIntanceEvent(grayInstanceService.findOneModel(policy.getInstanceId()));
        }
    }

    @Override
    public void saveGrayDecision(GrayDecision grayDecision) {
        grayDecisionService.saveModel(grayDecision);
        publishUpdateIntanceEvent(grayInstanceService.findOneModel(grayDecision.getInstanceId()));
    }

    @Override
    public void deleteGrayDecision(Long decisionId) {
        GrayDecision decision = grayDecisionService.findOneModel(decisionId);
        if (decision != null) {
            grayDecisionService.delete(decisionId);
            publishUpdateIntanceEvent(grayInstanceService.findOneModel(decision.getInstanceId()));
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

    @Override
    public void saveGrayService(GrayService grayPolicy) {
        grayServiceService.saveModel(grayPolicy);
    }

    @Override
    public GrayService getGrayService(String id) {
        return null;
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

    protected GrayEventPublisher getGrayEventPublisher() {
        return grayEventPublisher;
    }


    private void publishUpdateIntanceEvent(String serviceId, String instanceId) {
        publishGrayEvent(serviceId, instanceId, EventType.UPDATE);
    }


    private void publishDownIntanceEvent(String serviceId, String instanceId) {
        publishGrayEvent(serviceId, instanceId, EventType.DOWN);
    }

    private void publishUpdateIntanceEvent(GrayInstance grayInstance) {
        this.publishGrayEvent(grayInstance, EventType.UPDATE);
    }


    private void publishDownIntanceEvent(GrayInstance grayInstance) {
        publishGrayEvent(grayInstance, EventType.DOWN);
    }

    private void publishGrayEvent(GrayInstance grayInstance, EventType eventType) {
        publishGrayEvent(grayInstance.getServiceId(), grayInstance.getInstanceId(), eventType);
    }


    private void publishGrayEvent(String serviceId, String instanceId, EventType eventType) {
        GrayEventMsg eventMsg = new GrayEventMsg();
        eventMsg.setInstanceId(instanceId);
        eventMsg.setServiceId(serviceId);
        eventMsg.setEventType(eventType);
        eventMsg.setSourceType(SourceType.GRAY_INSTANCE);
        publishGrayEvent(eventMsg);
    }

    private void publishGrayEvent(GrayEventMsg eventMsg) {
        getGrayEventPublisher().publishEvent(eventMsg);

    }
}
