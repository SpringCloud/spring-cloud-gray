package cn.springcloud.gray.refresh;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoAware;
import cn.springcloud.gray.model.GrayInfos;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-04-24 09:32
 */
@Slf4j
public class GrayInformationRefresher implements Refresher, InstanceLocalInfoAware {

    public static final String TRIGGET_NAME = "REFRESH_GRAY_INFOS";

    private GrayManager grayManager;
    private GrayTrackHolder grayTrackHolder;
    private PolicyDecisionManager policyDecisionManager;
    private InformationClient informationClient;
    private InstanceLocalInfo instanceLocalInfo;
    private HandleManager handleManager;
    private HandleRuleManager handleRuleManager;


    public GrayInformationRefresher(
            GrayManager grayManager,
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager,
            HandleManager handleManager,
            HandleRuleManager handleRuleManager,
            InformationClient informationClient) {
        this.grayManager = grayManager;
        this.grayTrackHolder = grayTrackHolder;
        this.policyDecisionManager = policyDecisionManager;
        this.handleManager = handleManager;
        this.handleRuleManager = handleRuleManager;
        this.informationClient = informationClient;
    }

    @Override
    public boolean refresh() {
        grayTrackHolder.clearTrackDefinitions();
        grayManager.clearAllGrayServices();
        policyDecisionManager.clearAllPolicy();
        handleManager.clearAllHandleInfos();
        handleRuleManager.clearHandleRuleInfos();
        return load();
    }

    @Override
    public boolean load() {
        GrayInfos grayInfos = informationClient.allInfos(
                instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
        if (Objects.isNull(grayInfos)) {
            return false;
        }

        log.info("Load Gray Instances: {}", grayInfos.getInstances().size());
        grayInfos.getInstances().forEach(grayManager::updateGrayInstance);

        log.info("Load Gray Track Definitions: {}", grayInfos.getTrackDefinitions().size());
        grayInfos.getTrackDefinitions().forEach(grayTrackHolder::updateTrackDefinition);

        log.info("Load Policy Decisions: {}", grayInfos.getPolicyDecisions().size());
        grayInfos.getPolicyDecisions().forEach(policyDecisionManager::setPolicyDefinition);

        log.info("Load Service Route Infos: {}", grayInfos.getServiceRouteInfos().size());
        grayInfos.getServiceRouteInfos().forEach(grayManager::updateServiceRouteInfo);

        log.info("Load Handle Definitions: {}", grayInfos.getHandleDefinitions().size());
        grayInfos.getHandleDefinitions().forEach(handleManager::addHandleDefinition);

        log.info("Load Handle Rule Definitions: {}", grayInfos.getHandleRuleDefinitions().size());
        grayInfos.getHandleRuleDefinitions().forEach(handleRuleManager::putHandleRuleDefinition);

        publishRefreshedEvent(grayInfos);
        return true;
    }

    @Override
    public String triggerName() {
        return TRIGGET_NAME;
    }

    @Override
    public void setInstanceLocalInfo(InstanceLocalInfo instanceLocalInfo) {
        this.instanceLocalInfo = instanceLocalInfo;
    }


    private void publishRefreshedEvent(GrayInfos grayInfos) {
        GrayClientHolder.getSpringEventPublisher()
                .publishEvent(new GrayRefreshedEvent(TRIGGET_NAME, grayInfos));
    }
}
