package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.model.GrayInstanceAlias;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.gray.domain.query.GrayServiceQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface GrayServerModule {

    List<GrayService> allGrayServices();

    List<GrayInstance> listGrayInstancesByServiceId(String serviceId);

    List<GrayInstance> listGrayInstancesByServiceId(String serviceId, Collection<InstanceStatus> instanceStatus);

    List<GrayInstance> listGrayInstancesByStatus(GrayStatus grayStatus, Collection<InstanceStatus> instanceStatus);

    default void closeGray(String instanceId) {
        updateGrayStatus(instanceId, GrayStatus.CLOSE);
    }

    List<GrayInstance> listGrayInstancesByNormalInstanceStatus(Collection<InstanceStatus> instanceStatus);

    List<GrayInstance> listGrayInstances(Collection<String> serviceIds, Collection<InstanceStatus> instanceStatus);

    void deleteGrayService(String serviceId);

    default void instanceShutdown(String instanceId) {
        updateInstanceStatus(instanceId, InstanceStatus.DOWN);
    }

    default void openGray(String instanceId) {
        updateGrayStatus(instanceId, GrayStatus.OPEN);
    }

    void updateGrayStatus(String instanceId, GrayStatus grayStatus);

    GrayInstance saveGrayInstance(GrayInstance instance);

    void updateInstanceStatus(String instanceId, InstanceStatus instanceStatus);

    void updateInstanceStatus(GrayInstance instance, InstanceStatus instanceStatus);

    void deleteGrayInstance(String intanceId);

    GrayInstance getGrayInstance(String id);

    List<GrayService> listAllGrayServices();

    GrayService saveGrayService(GrayService grayPolicy);

    GrayService getGrayService(String id);

    default String getServiceContextPath(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        return Objects.isNull(grayService) ? "" : grayService.getContextPath();
    }

    Page<GrayService> listAllGrayServices(Pageable pageable);

    Page<GrayService> queryGrayServices(GrayServiceQuery query, Pageable pageable);

    List<GrayService> findGrayServices(Iterable<String> serviceIds);

    Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable);

    boolean isActiveGrayInstance(String instanceId);

    boolean isActiveGrayInstance(GrayInstance grayInstance);

    void closeGrayLock(String instanceId);

    void openGrayLock(String instanceId);

    void updateInstanceAliases(GrayInstanceAlias grayInstanceAlias, String currentUserId);
}
