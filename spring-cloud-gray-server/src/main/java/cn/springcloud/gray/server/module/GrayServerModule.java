package cn.springcloud.gray.server.module;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.server.module.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GrayServerModule {

    List<GrayService> allGrayServices();

    List<GrayInstance> listGrayInstancesBySerivceId(String serviceId);

    List<GrayInstance> listGrayInstancesByStatus(GrayStatus grayStatus, InstanceStatus instanceStatus);

    default void closeGray(String instanceId) {
        updateGrayStatus(instanceId, GrayStatus.CLOSE);
    }

    void deleteGrayService(String serviceId);

    default void instanceShutdown(String instanceId) {
        updateInstanceStatus(instanceId, InstanceStatus.DOWN);
    }

    default void openGray(String instanceId) {
        updateGrayStatus(instanceId, GrayStatus.OPEN);
    }

    void updateGrayStatus(String instanceId, GrayStatus grayStatus);

    void saveGrayInstance(GrayInstance instance);

    void updateInstanceStatus(String instanceId, InstanceStatus instanceStatus);

    void deleteGrayInstance(String intanceId);

    void saveGrayPolicy(GrayPolicy grayPolicy);

    void deleteGrayPolicy(Long policyId);

    void saveGrayDecision(GrayDecision grayDecision);

    void deleteGrayDecision(Long decisionId);

    GrayDecision getGrayDecision(Long id);

    List<GrayDecision> listGrayDecisionsByPolicyId(Long policyId);

    List<GrayInstance> listGrayInstancesByServiceId(String serviceId);

    GrayInstance getGrayInstance(String id);

    List<GrayService> listAllGrayServices();

    void saveGrayService(GrayService grayPolicy);

    GrayService getGrayService(String id);

    List<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId);

    Page<GrayService> listAllGrayServices(Pageable pageable);

    Page<GrayPolicy> listGrayPoliciesByInstanceId(String instanceId, Pageable pageable);

    Page<GrayInstance> listGrayInstancesByServiceId(String serviceId, Pageable pageable);

    Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable);
}
