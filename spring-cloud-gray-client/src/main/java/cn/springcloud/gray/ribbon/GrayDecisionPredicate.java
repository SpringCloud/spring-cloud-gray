package cn.springcloud.gray.ribbon;

import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.gray.core.GrayDecision;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import java.util.List;

public class GrayDecisionPredicate extends AbstractServerPredicate {

    public GrayDecisionPredicate(GrayLoadBalanceRule rule) {
        super(rule);
    }

    @Override
    public boolean apply(PredicateKey input) {
        BambooRequestContext bambooRequestContext = BambooRequestContext.currentRequestCentxt();
        if (bambooRequestContext == null || bambooRequestContext.getBambooRequest() == null) {
            return false;
        }
        DiscoveryEnabledServer server = (DiscoveryEnabledServer) input.getServer();
        BambooRequest bambooRequest = bambooRequestContext.getBambooRequest();
        List<GrayDecision> grayDecisions =
                getIRule().getGrayManager().grayDecision(bambooRequest.getServiceId(), server.getInstanceInfo().getInstanceId());
        for (GrayDecision grayDecision : grayDecisions) {
            if (grayDecision.test(bambooRequest)) {
                return true;
            }
        }
        return false;
    }


    protected GrayLoadBalanceRule getIRule() {
        return (GrayLoadBalanceRule) this.rule;
    }


}
