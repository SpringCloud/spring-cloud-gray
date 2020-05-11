package cn.springcloud.gray.refresh;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoAware;
import cn.springcloud.gray.model.GrayInfos;
import cn.springcloud.gray.request.track.GrayTrackHolder;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-04-24 09:32
 */
public class GrayInformationRefresher implements Refresher, InstanceLocalInfoAware {

    public static final String TRIGGET_NAME = "REFRESH_GRAY_INFOS";

    private GrayManager grayManager;
    private GrayTrackHolder grayTrackHolder;
    private PolicyDecisionManager policyDecisionManager;
    private InformationClient informationClient;
    private InstanceLocalInfo instanceLocalInfo;


    public GrayInformationRefresher(
            GrayManager grayManager,
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager,
            InformationClient informationClient) {
        this.grayManager = grayManager;
        this.grayTrackHolder = grayTrackHolder;
        this.policyDecisionManager = policyDecisionManager;
        this.informationClient = informationClient;
    }

    @Override
    public boolean refresh() {
        grayTrackHolder.clearTrackDefinitions();
        grayManager.clearAllGrayServices();
        policyDecisionManager.clearAllPolicy();
        return load();
    }

    @Override
    public boolean load() {
        GrayInfos grayInfos = informationClient.allInfos(
                instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
        if (Objects.isNull(grayInfos)) {
            return false;
        }
        grayInfos.getInstances().forEach(grayManager::updateGrayInstance);
        grayInfos.getTrackDefinitions().forEach(grayTrackHolder::updateTrackDefinition);
        grayInfos.getPolicyDecisions().forEach(policyDecisionManager::setPolicyDefinition);
        grayInfos.getServiceRouteInfos().forEach(grayManager::updateServiceRouteInfo);

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
