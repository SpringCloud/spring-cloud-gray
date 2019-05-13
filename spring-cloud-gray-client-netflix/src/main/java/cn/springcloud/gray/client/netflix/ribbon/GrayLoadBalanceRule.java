package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.GrayClientHolder;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.google.common.base.Optional;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 灰度发布的负载规则
 */
@Slf4j
public class GrayLoadBalanceRule extends ZoneAvoidanceRule {

    protected CompositePredicate grayCompositePredicate;
    protected GrayManager grayManager;
    protected RequestLocalStorage requestLocalStorage;
    protected ServerExplainer<Server> serverExplainer;


    public GrayLoadBalanceRule() {
        this(GrayClientHolder.getGrayManager(), GrayClientHolder.getRequestLocalStorage(),
                GrayClientHolder.getServerExplainer());
    }

    public GrayLoadBalanceRule(GrayManager grayManager, RequestLocalStorage requestLocalStorage,
                               ServerExplainer<Server> serverExplainer) {
        super();
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.serverExplainer = serverExplainer;
        init();
    }

//    public GrayLoadBalanceRule() {
//        super();
//        init();
//    }

    protected void init() {
        GrayDecisionPredicate grayPredicate = new GrayDecisionPredicate(this);
        grayCompositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                grayPredicate).build();
    }


    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        String serviceId = grayRequest.getServiceId();
        if (grayManager.hasGray(serviceId)) {
            GrayService grayService = grayManager.getGrayService(serviceId);
            List<Server> servers = lb.getAllServers();
            List<Server> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
            List<Server> normalServers = new ArrayList<>(servers.size() - grayService.getGrayInstances().size());

            for (Server server : servers) {
//                ServerSpec serverSpec = serverExplainer.apply(server);
                if (grayService.getGrayInstance(server.getMetaInfo().getInstanceId()) != null) {
                    grayServers.add(server);
                } else {
                    normalServers.add(server);
                }
            }
            Optional<Server> server = grayCompositePredicate.chooseRoundRobinAfterFiltering(grayServers, key);
            if (server.isPresent()) {
                return expect(server.get());
            } else {
                return expect(choose(super.getPredicate(), normalServers, key));
            }

        } else {
            return expect(super.choose(key));
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
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
        return grayManager;
    }

    public RequestLocalStorage getRequestLocalStorage() {
        return requestLocalStorage;
    }

    public ServerExplainer<Server> getServerExplainer() {
        return serverExplainer;
    }

    private Server expect(Server server) {
        if (server != null) {
            log.debug("找到server:{}", server.getId());
        }
        return server;
    }
}

