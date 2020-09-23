package cn.springcloud.gray.choose;

import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.request.GrayRequest;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean testPolicies(Collection<Policy> policies, DecisionInputArgs decisionInputArgs) {
        if (Objects.isNull(policies) || policies.size() < 1) {
            return false;
        }
        for (Policy policy : policies) {
            if (policy.predicateDecisions(decisionInputArgs)) {
                return true;
            }
        }
        return false;
    }
}
