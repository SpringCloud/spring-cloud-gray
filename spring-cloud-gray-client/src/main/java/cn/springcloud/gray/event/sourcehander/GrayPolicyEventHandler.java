package cn.springcloud.gray.event.sourcehander;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.PolicyDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GrayPolicyEventHandler implements GraySourceEventHandler {

    private static final Logger log = LoggerFactory.getLogger(GrayPolicyEventHandler.class);

    private PolicyDecisionManager policyDecisionManager;

    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    public GrayPolicyEventHandler(PolicyDecisionManager policyDecisionManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.policyDecisionManager = policyDecisionManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    public void handle(GrayEventMsg eventMsg) {
        if (!Objects.equals(eventMsg.getSourceType(), SourceType.GRAY_POLICY)) {
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

        PolicyDefinition policyDefinition = (PolicyDefinition) eventMsg.getSource();

        if (Objects.equals(eventMsg.getEventType(), EventType.UPDATE)) {
            policyDecisionManager.setPolicyDefinition(policyDefinition);
        } else {
            policyDecisionManager.removePolicy(policyDefinition.getPolicyId());
        }
    }
}
