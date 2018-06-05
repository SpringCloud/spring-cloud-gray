package cn.springcloud.gray.ribbon;

import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.gray.core.GrayDecision;
import cn.springcloud.gray.utils.ServiceUtil;
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
        BambooRequestContext bambooRequestContext = BambooRequestContext.currentRequestCentxt();
        if (bambooRequestContext == null || bambooRequestContext.getBambooRequest() == null) {
            return false;
        }
        Server server = input.getServer();
        String instanceId = ServiceUtil.getInstanceId(server);

        BambooRequest bambooRequest = bambooRequestContext.getBambooRequest();
        List<GrayDecision> grayDecisions =
                getIRule().getGrayManager().grayDecision(bambooRequest.getServiceId(), instanceId);
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
