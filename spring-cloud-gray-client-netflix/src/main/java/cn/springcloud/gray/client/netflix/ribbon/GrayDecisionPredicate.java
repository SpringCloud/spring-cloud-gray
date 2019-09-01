package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.servernode.ServerSpec;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GrayDecisionPredicate extends AbstractServerPredicate {

    private static final Logger log = LoggerFactory.getLogger(GrayDecisionPredicate.class);

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
        try {
            ServerSpec serverSpec = grayRule.getServerExplainer().apply(server);

            GrayDecisionInputArgs decisionInputArgs = GrayDecisionInputArgs
                    .builder().grayRequest(grayRequest).server(serverSpec).build();

            List<GrayDecision> grayDecisions = grayRule.getGrayManager().getGrayDecision(serviceId, instanceId);

            for (GrayDecision grayDecision : grayDecisions) {
                if (grayDecision.test(decisionInputArgs)) {
                    return true;
                }
            }
        }catch (Exception e){
            log.error("", e);
        }
        return false;
    }


    protected GrayLoadBalanceRule getIRule() {
        return (GrayLoadBalanceRule) this.rule;
    }

}