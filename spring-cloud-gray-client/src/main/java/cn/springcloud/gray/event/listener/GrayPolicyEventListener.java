package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.event.GrayPolicyEvent;

/**
 * @author saleson
 * @date 2020-02-03 18:18
 */
public class GrayPolicyEventListener extends AbstractGrayEventListener<GrayPolicyEvent> {


    private PolicyDecisionManager policyDecisionManager;


    public GrayPolicyEventListener(PolicyDecisionManager policyDecisionManager) {
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected void onUpdate(GrayPolicyEvent event) {
        policyDecisionManager.setPolicyDefinition(event.getSource());
    }

    @Override
    protected void onDelete(GrayPolicyEvent event) {
        policyDecisionManager.removePolicy(event.getSourceId());
    }

}
