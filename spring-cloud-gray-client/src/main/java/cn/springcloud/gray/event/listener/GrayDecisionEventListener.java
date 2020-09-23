package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.event.GrayDecisionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-02-03 18:18
 */
@Slf4j
public class GrayDecisionEventListener extends AbstractGrayEventListener<GrayDecisionEvent> {

    private PolicyDecisionManager policyDecisionManager;

    public GrayDecisionEventListener(PolicyDecisionManager policyDecisionManager) {
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected void onUpdate(GrayDecisionEvent event) {
        DecisionDefinition decisionDefinition = event.getSource();
        policyDecisionManager.setDecisionDefinition(event.getPolicyId(), decisionDefinition);
    }

    @Override
    protected void onDelete(GrayDecisionEvent event) {
        DecisionDefinition decisionDefinition = event.getSource();
        policyDecisionManager.removeDecisionDefinition(event.getPolicyId(), decisionDefinition.getId());
    }

}
