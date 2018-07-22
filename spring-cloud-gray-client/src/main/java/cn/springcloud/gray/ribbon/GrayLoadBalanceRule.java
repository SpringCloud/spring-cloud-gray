package cn.springcloud.gray.ribbon;

import cn.springcloud.bamboo.BambooAppContext;
import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.bamboo.ribbon.loadbalancer.BambooZoneAvoidanceRule;
import cn.springcloud.gray.client.GrayClientAppContext;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.core.GrayManager;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.utils.ServiceUtil;
import com.google.common.base.Optional;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 灰度发布的负载规则
 */
public class GrayLoadBalanceRule extends ZoneAvoidanceRule {

    protected CompositePredicate grayCompositePredicate;
    protected ZoneAvoidanceRule subDelegate;

    public GrayLoadBalanceRule() {
        super();
        init();
    }

    protected void init(){
        GrayDecisionPredicate apiVersionPredicate = new GrayDecisionPredicate(this);
        GrayClientProperties grayClientProperties = GrayClientAppContext.getGrayClientProperties();
        //如果与多版本一起使用
        if(grayClientProperties.getInstance().isUseMultiVersion()){
            subDelegate = new BambooZoneAvoidanceRule();
            grayCompositePredicate = CompositePredicate.withPredicates(subDelegate.getPredicate(),
                    apiVersionPredicate).build();
        }else{
            grayCompositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                    apiVersionPredicate).build();
        }
    }


    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        BambooRequestContext requestContext = BambooRequestContext.currentRequestCentxt();
        String serviceId = requestContext.getServiceId();
        if (requestContext != null && getGrayManager().isOpen(serviceId)) {
            GrayService grayService = getGrayManager().grayService(serviceId);
            List<Server> servers = lb.getAllServers();
            List<Server> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
            List<Server> normalServers = new ArrayList<>(servers.size() - grayService.getGrayInstances().size());
            for (Server server : servers) {
                Map<String, String> serverMetadata = getServerMetadata(serviceId, server);
                String instanceId = ServiceUtil.getInstanceId(server, serverMetadata);
                if (grayService.getGrayInstance(instanceId) != null) {
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
        }else if(subDelegate!=null){
            return subDelegate.choose(key);
        }else{
            return super.choose(key);
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        if(subDelegate!=null){
            subDelegate.initWithNiwsConfig(clientConfig);
        }
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if(subDelegate!=null){
            subDelegate.setLoadBalancer(lb);
        }
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

    public static Map<String, String> getServerMetadata(String serviceId, Server server) {
        return BambooAppContext.getEurekaServerExtractor().getServerMetadata(serviceId, server);
    }
}
