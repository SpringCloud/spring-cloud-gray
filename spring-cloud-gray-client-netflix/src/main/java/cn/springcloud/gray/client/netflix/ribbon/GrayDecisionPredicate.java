package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.request.GrayRequest;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class GrayDecisionPredicate extends AbstractServerPredicate {

    public GrayDecisionPredicate(GrayLoadBalanceRule rule) {
        super(rule);
    }

    @Override
    public boolean apply(PredicateKey input) {

        GrayLoadBalanceRule grayRule = getIRule();
        GrayRequest grayRequest = grayRule.getRequestLocalStorage().getGrayRequest();

        Server server = input.getServer();
        String serviceId = grayRequest.getServiceId();
        String instanceId = server.getMetaInfo().getInstanceId();
        List<GrayDecision> grayDecisions = grayRule.getGrayManager().getGrayDecision(serviceId, instanceId);

        ServerSpec serverSpec = grayRule.getServerExplainer().apply(server);

        for (GrayDecision grayDecision : grayDecisions) {
            if (grayDecision.test(GrayDecisionInputArgs.builder().grayRequest(grayRequest).server(serverSpec).build())) {
                return true;
            }
        }
        return false;
    }


    protected GrayLoadBalanceRule getIRule() {
        return (GrayLoadBalanceRule) this.rule;
    }

}