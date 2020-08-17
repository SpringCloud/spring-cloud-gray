package cn.springcloud.gray.choose;

import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.Policy;

import java.util.Collections;
import java.util.List;

public class SimplePolicyPredicate extends AbstractPolicyPredicate {


    @Override
    protected List<Policy> getPolicies(DecisionInputArgs decisionInputArgs) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String predicateType() {
        return PredicateType.SIMPLE.name();
    }
}
