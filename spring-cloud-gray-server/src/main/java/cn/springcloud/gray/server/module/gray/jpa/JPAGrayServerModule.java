package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.model.GrayInstanceAlias;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.gray.domain.query.GrayServiceQuery;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayServiceService;
import cn.springlcoud.gray.event.server.GrayEventTrigger;
import cn.springlcoud.gray.event.server.TriggerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JPAGrayServerModule implements GrayServerModule {

    private GrayServiceService grayServiceService;
    private GrayInstanceService grayInstanceService;
    private GrayEventTrigger grayEventTrigger;
    private GrayServerProperties grayServerProperties;
    private ServiceDiscovery serviceDiscovery;
    private ServiceManageModule serviceManageModule;

    public JPAGrayServerModule(
            GrayServerProperties grayServerProperties,
            GrayEventTrigger grayEventTrigger,
            ServiceDiscovery serviceDiscovery,
            GrayServiceService grayServiceService,
            GrayInstanceService grayInstanceService,
            ServiceManageModule serviceManageModule) {
        this.grayServerProperties = grayServerProperties;
        this.grayEventTrigger = grayEventTrigger;
        this.serviceDiscovery = serviceDiscovery;
        this.grayServiceService = grayServiceService;
        this.grayInstanceService = grayInstanceService;
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

    @Override
    public List<GrayInstance> listGrayInstances(Collection<String> serviceIds, Collection<InstanceStatus> instanceStatus) {
        return grayInstanceService.listGrayInstances(serviceIds, instanceStatus);
    }


    @Transactional
    @Override
    public void deleteGrayService(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        if (Objects.isNull(grayService)) {
            return;
        }
        grayServiceService.deleteReactById(serviceId);
//        List<GrayInstance> grayInstances = grayInstanceService.findByServiceId(serviceId);
//        grayInstances.forEach(this::publishDownIntanceEvent);
        serviceManageModule.deleteSericeManeges(serviceId);
        triggerDeleteEvent(grayService);
    }


    @Override
    public void updateGrayStatus(String instanceId, GrayStatus grayStatus) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (instance == null || Objects.equals(instance.getGrayStatus(), grayStatus)) {
            return;
        }

        instance.setGrayStatus(grayStatus);
        grayInstanceService.saveModel(instance);

        if (grayStatus == GrayStatus.OPEN) {
            triggerUpdateEvent(instance);
        } else {
            triggerDeleteEvent(instance);
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
                    .namespace("default")
                    .build();
            grayServiceService.saveModel(grayService);
            serviceManageModule.insertServiceOwner(grayService.getServiceId());
        }

        GrayInstance oldRecord = grayInstanceService.findOneModel(instance.getInstanceId());
        if (Objects.isNull(instance.getInstanceStatus()) && !Objects.isNull(serviceDiscovery)) {
            InstanceInfo instanceInfo =
                    serviceDiscovery.getInstanceInfo(instance.getServiceId(), instance.getInstanceId());
            if (Objects.nonNull(instanceInfo)) {
                instance.setInstanceStatus(instanceInfo.getInstanceStatus());
                if (Objects.isNull(oldRecord) && Objects.nonNull(instanceInfo.getAliases()) && instanceInfo.getAliases().length > 0) {
                    instance.setAliases(instanceInfo.getAliases());
                }
            }
        }

        GrayInstance newRecord = grayInstanceService.saveModel(instance);
        if (isLockGray(newRecord) ||
                grayServerProperties.getInstance().getNormalInstanceStatus().contains(instance.getInstanceStatus())) {
            if (Objects.equals(newRecord.getGrayStatus(), GrayStatus.OPEN)) {
                triggerUpdateEvent(instance);
            } else if (oldRecord != null && Objects.equals(oldRecord.getGrayStatus(), GrayStatus.OPEN)) {
                triggerDeleteEvent(instance);
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
                    triggerUpdateEvent(instance);
                } else {
                    triggerDeleteEvent(instance);
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
        }
    }

    private boolean isLockGray(GrayInstance instance) {
        return Objects.equals(instance.getGrayLock(), GrayInstance.GRAY_LOCKED);
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
        triggerUpdateEvent(record);
        return record;
    }

    @Override
    public GrayService getGrayService(String id) {
        return grayServiceService.findOneModel(id);
    }

    @Override
    public Page<GrayService> listAllGrayServices(Pageable pageable) {
        return grayServiceService.listAllGrayServices(pageable);
    }

    @Override
    public Page<GrayService> queryGrayServices(GrayServiceQuery query, Pageable pageable) {
        return grayServiceService.queryGrayServices(query, pageable);
    }

    @Override
    public List<GrayService> findGrayServices(Iterable<String> serviceIds) {
        return grayServiceService.findAllModel(serviceIds);
    }

    @Override
    public Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable) {
        return grayInstanceService.listGrayInstancesByServiceId(serviceId, pageable);
    }

    protected GrayEventTrigger getGrayEventTrigger() {
        return grayEventTrigger;
    }


    protected void triggerEvent(TriggerType triggerType, Object source) {
        getGrayEventTrigger().triggering(source, triggerType);
    }

    protected void triggerDeleteEvent(Object source) {
        triggerEvent(TriggerType.DELETE, source);
    }

    protected void triggerUpdateEvent(Object source) {
        if (source instanceof GrayInstance && !isActiveGrayInstance((GrayInstance) source)) {
            GrayInstance grayInstance = (GrayInstance) source;
            log.info("服务{} 实例{}非灰度状态，不同步事件信息。", grayInstance.getServiceId(), grayInstance.getInstanceId());
            return;
        }
        triggerEvent(TriggerType.MODIFY, source);
    }


    @Override
    public boolean isActiveGrayInstance(String instanceId) {
        GrayInstance grayInstance = getGrayInstance(instanceId);
        if (Objects.isNull(grayInstance)) {
            return false;
        }
        return isActiveGrayInstance(grayInstance);
    }

    @Override
    public boolean isActiveGrayInstance(GrayInstance grayInstance) {
        return Objects.equals(grayInstance.getGrayStatus(), GrayStatus.OPEN)
                && (
                isLockGray(grayInstance) ||
                        grayServerProperties.getInstance().getNormalInstanceStatus().contains(grayInstance.getInstanceStatus()));
    }

    @Override
    public void closeGrayLock(String instanceId) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (Objects.isNull(instance) || Objects.equals(instance.getGrayLock(), GrayInstance.GRAY_UNLOCKED)) {
            return;
        }

        instance.setGrayLock(GrayInstance.GRAY_UNLOCKED);
        grayInstanceService.saveModel(instance);

        //推送实例变更信息
        if (Objects.equals(instance.getGrayStatus(), GrayStatus.CLOSE)) {
            return;
        }

        boolean isNormalInstanceStatus = grayServerProperties.getInstance()
                .getNormalInstanceStatus()
                .contains(instance.getInstanceStatus());
        if (!isNormalInstanceStatus) {
            triggerDeleteEvent(instance);
        }
    }

    @Override
    public void openGrayLock(String instanceId) {
        GrayInstance instance = grayInstanceService.findOneModel(instanceId);
        if (Objects.isNull(instance) || Objects.equals(instance.getGrayLock(), GrayInstance.GRAY_LOCKED)) {
            return;
        }
        instance.setGrayLock(GrayInstance.GRAY_LOCKED);
        grayInstanceService.saveModel(instance);

        //推送实例变更信息
        if (Objects.equals(instance.getGrayStatus(), GrayStatus.CLOSE)) {
            return;
        }

        boolean isNormalInstanceStatus = grayServerProperties.getInstance()
                .getNormalInstanceStatus()
                .contains(instance.getInstanceStatus());
        if (!isNormalInstanceStatus) {
            triggerEvent(TriggerType.MODIFY, instance);
        }
    }

    @Override
    public void updateInstanceAliases(GrayInstanceAlias grayInstanceAlias, String currentUserId) {
        GrayInstance grayInstance = grayInstanceService.findOneModel(grayInstanceAlias.getInstanceId());
        grayInstance.setAliases(grayInstanceAlias.getAliases());
        grayInstance.setOperator(currentUserId);
        grayInstanceService.saveModel(grayInstance);
        if (isActiveGrayInstance(grayInstance)) {
            triggerUpdateEvent(grayInstanceAlias);
        }
    }
}
