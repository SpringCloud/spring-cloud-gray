package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GrayServerPredicate extends AbstractPolicyPredicate {

    private GrayManager grayManager;
    private PolicyDecisionManager policyDecisionManager;


    public GrayServerPredicate(GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        this.grayManager = grayManager;
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected List<Policy> getPolicies(DecisionInputArgs decisionInputArgs) {
        GrayDecisionInputArgs grayDecisionInputArgs = (GrayDecisionInputArgs) decisionInputArgs;
        ServerSpec serverSpec = grayDecisionInputArgs.getServer();
        Collection<String> policieIds = grayManager.getInstanceRoutePolicies(serverSpec.getServiceId(), serverSpec.getInstanceId());
        if (CollectionUtils.isEmpty(policieIds)) {
            return Collections.EMPTY_LIST;
        }
        return policyDecisionManager.getPolicies(policieIds);
    }

    @Override
    public String predicateType() {
        return PredicateType.SERVER.name();
    }
}
