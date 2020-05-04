package cn.springcloud.gray.choose;

import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.request.GrayRequest;

import java.util.List;

/**
 * @author saleson
 * @date 2020-04-08 00:23
 */
public abstract class AbstractPolicyPredicate implements PolicyPredicate {

    @Override
    public boolean test(DecisionInputArgs decisionInputArgs) {
        GrayRequest grayRequest = decisionInputArgs.getGrayRequest();
        if (grayRequest == null) {
            return false;
        }
        return testPolicies(getPolicies(decisionInputArgs), decisionInputArgs);
    }


    protected abstract List<Policy> getPolicies(DecisionInputArgs decisionInputArgs);

    protected boolean testPolicies(List<Policy> policies, DecisionInputArgs decisionInputArgs) {
        for (Policy policy : policies) {
            if (!policy.predicateDecisions(decisionInputArgs)) {
                return false;
            }
        }
        return true;
    }
}
