package cn.springcloud.gray.ribbon;

import cn.springcloud.bamboo.BambooAppContext;
import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.gray.core.GrayDecision;
import cn.springcloud.gray.utils.ServiceUtil;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Map;

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
        BambooRequest bambooRequest = bambooRequestContext.getBambooRequest();
        Server server = input.getServer();
        String serviceId = bambooRequest.getServiceId();
        Map<String, String> serverMetadata = getServerMetadata(serviceId, server);
        String instanceId = ServiceUtil.getInstanceId(server, serverMetadata);

        List<GrayDecision> grayDecisions =
                getIRule().getGrayManager().grayDecision(serviceId, instanceId);
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

    public Map<String, String> getServerMetadata(String serviceId, Server server) {
        return BambooAppContext.getEurekaServerExtractor().getServerMetadata(serviceId, server);
    }
}
