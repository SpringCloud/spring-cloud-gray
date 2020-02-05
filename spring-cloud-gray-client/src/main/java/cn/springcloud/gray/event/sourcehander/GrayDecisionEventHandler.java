package cn.springcloud.gray.event.sourcehander;

import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.DecisionDefinitionMsg;
import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.DecisionDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GrayDecisionEventHandler implements GraySourceEventHandler {

    private static final Logger log = LoggerFactory.getLogger(GrayDecisionEventHandler.class);

    private UpdateableGrayManager grayManager;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    public GrayDecisionEventHandler(UpdateableGrayManager grayManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    public void handle(GrayEventMsg eventMsg) {
        if (!Objects.equals(eventMsg.getSourceType(), SourceType.GRAY_DECISION)) {
            return;
        }

        if (eventMsg.getSource() == null) {
            throw new NullPointerException("event source is null");
        }

        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            if (StringUtils.equals(eventMsg.getServiceId(), instanceLocalInfo.getServiceId())) {
                return;
            }
        }


        DecisionDefinitionMsg decisionDefinitionMsg = (DecisionDefinitionMsg) eventMsg.getSource();

        if (Objects.equals(eventMsg.getEventType(), EventType.UPDATE)) {
            DecisionDefinition decisionDefinition = new DecisionDefinition();
            decisionDefinition.setInfos(decisionDefinitionMsg.getInfos());
            decisionDefinition.setName(decisionDefinitionMsg.getName());
            decisionDefinition.setId(decisionDefinitionMsg.getId());
            grayManager.updateDecisionDefinition(
                    eventMsg.getServiceId(), eventMsg.getInstanceId(), decisionDefinitionMsg.getPolicyId(), decisionDefinition);
        } else {
            grayManager.removeDecisionDefinition(
                    eventMsg.getServiceId(), eventMsg.getInstanceId(), decisionDefinitionMsg.getPolicyId(), decisionDefinitionMsg.getId());
        }
    }
}
