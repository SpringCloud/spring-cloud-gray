package cn.springcloud.gray.server.module;

import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.server.module.domain.*;

import java.util.List;

public interface GrayModule {


    List<GrayService> allGrayService();

    List<GrayInstance> listGrayInstanceBySerivceId(String serviceId);

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

    void addGrayService(GrayService service);

    void addGrayInstance(GrayInstance instance);

    void updateGrayInstance(GrayInstance instance);

    void updateInstanceStatus(String instanceId, InstanceStatus instanceStatus);

    void deleteGrayInstance(String intanceId);

    void addGrayPolicy(GrayPolicy grayPolicy);

    void updateGrayPolicy(GrayPolicy grayPolicy);

    void deleteGrayPolicy(Long policyId);

    void addGrayDecision(GrayDecision grayDecision);

    void updateGrayDecision(GrayDecision grayDecision);

    void deleteGrayDecision(Long decisionId);


    List<cn.springcloud.gray.model.GrayInstance> allOpenInstances();


}
