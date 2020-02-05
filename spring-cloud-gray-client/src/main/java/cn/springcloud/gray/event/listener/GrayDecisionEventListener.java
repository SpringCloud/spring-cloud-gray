package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springlcoud.gray.event.GrayDecisionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2020-02-03 18:18
 */
@Slf4j
public class GrayDecisionEventListener extends AbstractGrayEventListener<GrayDecisionEvent> {

    private UpdateableGrayManager grayManager;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    public GrayDecisionEventListener(
            UpdateableGrayManager grayManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    protected void onUpdate(GrayDecisionEvent event) {
        DecisionDefinition decisionDefinition = event.getSource();
        grayManager.updateDecisionDefinition(
                event.getServiceId(), event.getInstanceId(), event.getPolicyId(), decisionDefinition);
    }

    @Override
    protected void onDelete(GrayDecisionEvent event) {
        DecisionDefinition decisionDefinition = event.getSource();
        grayManager.removeDecisionDefinition(
                event.getServiceId(), event.getInstanceId(), event.getPolicyId(), decisionDefinition.getId());
    }


    protected boolean validate(GrayDecisionEvent event) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            if (StringUtils.equals(event.getServiceId(), instanceLocalInfo.getServiceId())) {
                return false;
            }
        }
        return true;
    }
}
