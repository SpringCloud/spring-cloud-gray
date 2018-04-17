package cn.springcloud.gray.ribbon;

import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.bamboo.ribbon.loadbalancer.BambooZoneAvoidanceRule;
import cn.springcloud.gray.client.GrayClientAppContext;
import cn.springcloud.gray.core.GrayManager;
import cn.springcloud.gray.core.GrayService;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import java.util.ArrayList;
import java.util.List;


/**
 * 灰度发布的负载规则
 */
public class GrayLoadBalanceRule extends BambooZoneAvoidanceRule {

    protected CompositePredicate grayCompositePredicate;

    public GrayLoadBalanceRule() {
        super();
        GrayDecisionPredicate apiVersionPredicate = new GrayDecisionPredicate(this);
        grayCompositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                apiVersionPredicate).build();
    }


    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        BambooRequestContext requestContext = BambooRequestContext.currentRequestCentxt();
        if (requestContext != null && getGrayManager().isOpen(requestContext.getServiceId())) {
            GrayService grayService = getGrayManager().grayService(requestContext.getServiceId());
            List<Server> servers = lb.getAllServers();
            List<Server> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
            List<Server> normalServers = new ArrayList<>(servers.size() - grayService.getGrayInstances().size());
            for (Server server : servers) {
                DiscoveryEnabledServer disServer = (DiscoveryEnabledServer) server;
                if (grayService.getGrayInstance(disServer.getInstanceInfo().getInstanceId()) != null) {
                    grayServers.add(server);
                } else {
                    normalServers.add(server);
                }
            }

            Optional<Server> server = grayCompositePredicate.chooseRoundRobinAfterFiltering(grayServers, key);
            if (server.isPresent()) {
                return server.get();
            } else {
                return choose(super.getPredicate(), normalServers, key);
            }
        }
        return super.choose(key);
    }


    private Server choose(AbstractServerPredicate serverPredicate, List<Server> servers, Object key) {
        Optional<Server> server = serverPredicate.chooseRoundRobinAfterFiltering(servers, key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }


    public GrayManager getGrayManager() {
        return GrayClientAppContext.getGrayManager();
    }
}
