package cn.springcloud.gray.decision;

import cn.springcloud.gray.model.PolicyDefinition;

/**
 * @author saleson
 * @date 2020-07-28 19:22
 */
public class BringPolicyDefinitionGrayDecision extends GrayDecisionDelegater {

    private PolicyDefinition policyDefinition;

    public BringPolicyDefinitionGrayDecision(GrayDecision delegate, PolicyDefinition policyDefinition) {
        super(delegate);
        this.policyDefinition = policyDefinition;
    }


    public PolicyDefinition getPolicyDefinition() {
        return policyDefinition;
    }
}
