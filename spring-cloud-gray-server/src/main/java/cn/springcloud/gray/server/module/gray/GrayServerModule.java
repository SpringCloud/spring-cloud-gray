package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface GrayServerModule {

    List<GrayService> allGrayServices();

    List<GrayInstance> listGrayInstancesByServiceId(String serviceId);

    List<GrayInstance> listGrayInstancesByServiceId(String serviceId, Collection<InstanceStatus> instanceStatus);

    List<GrayInstance> listGrayInstancesByStatus(GrayStatus grayStatus, Collection<InstanceStatus> instanceStatus);

    default void closeGray(String instanceId) {
        updateGrayStatus(instanceId, GrayStatus.CLOSE);
    }

    List<GrayInstance> listGrayInstancesByNormalInstanceStatus(Collection<InstanceStatus> instanceStatus);

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

    GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy);

    void deleteGrayPolicy(Long policyId);

    GrayDecision saveGrayDecision(GrayDecision grayDecision);

    void deleteGrayDecision(Long decisionId);

    GrayDecision getGrayDecision(Long id);

    List<GrayDecision> listGrayDecisionsByPolicyId(Long policyId);


    GrayInstance getGrayInstance(String id);

    List<GrayService> listAllGrayServices();

    GrayService saveGrayService(GrayService grayPolicy);

    GrayService getGrayService(String id);

    String getServiceContextPath(String serviceId);

    List<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId);

    Page<GrayService> listAllGrayServices(Pageable pageable);

    List<GrayService> findGrayServices(Iterable<String> serviceIds);

    Page<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId, Pageable pageable);

    Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable);

    Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable);
}
