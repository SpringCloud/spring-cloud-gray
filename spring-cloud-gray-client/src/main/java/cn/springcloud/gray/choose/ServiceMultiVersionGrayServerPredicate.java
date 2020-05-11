package cn.springcloud.gray.choose;

import cn.springcloud.gray.DataSet;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ServiceMultiVersionGrayServerPredicate extends AbstractPolicyPredicate {

    private GrayManager grayManager;
    private PolicyDecisionManager policyDecisionManager;


    public ServiceMultiVersionGrayServerPredicate(GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        this.grayManager = grayManager;
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected List<Policy> getPolicies(DecisionInputArgs decisionInputArgs) {
        GrayDecisionInputArgs grayDecisionInputArgs = (GrayDecisionInputArgs) decisionInputArgs;
        ServerSpec serverSpec = grayDecisionInputArgs.getServer();
        if (StringUtils.isEmpty(serverSpec.getVersion())) {
            return Collections.EMPTY_LIST;
        }
        DataSet<String> policieIds = grayManager.getServiceVersionRoutePolicies(serverSpec.getServiceId(), serverSpec.getVersion());
        if (Objects.isNull(policieIds) || policieIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return policyDecisionManager.getPolicies(policieIds.getDatas());
    }

    @Override
    public String predicateType() {
        return PredicateType.SERVICE_MULTI_VERSION_SERVER.name();
    }
}
