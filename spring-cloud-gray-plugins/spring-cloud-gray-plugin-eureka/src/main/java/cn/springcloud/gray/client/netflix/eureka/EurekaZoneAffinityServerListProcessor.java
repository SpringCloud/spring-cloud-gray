package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.ServerListProcessor;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.discovery.shared.Application;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EurekaZoneAffinityServerListProcessor extends EurekaServerListProcessor {


    private volatile String zone;

    public EurekaZoneAffinityServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, EurekaClient eurekaClient) {
        super(grayHoldoutServerProperties, eurekaClient);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        zone = ConfigurationManager.getDeploymentContext().getValue(DeploymentContext.ContextKey.zone);
        super.afterPropertiesSet();
    }

    @Override
    protected List<Server> getUnUpServerList(String serviceId, List<InstanceStatus> instanceStatuses) {
        List<Server> unUpServers =  super.getUnUpServerList(serviceId, instanceStatuses);
        return getFilteredListOfServers(unUpServers);
//        if(CollectionUtils.isEmpty(filtereds)){
//            return unUpServers;
//        }
//
//        return filtereds;
    }

    private List<Server> getFilteredListOfServers(List<Server> servers){
        return servers.stream()
                .filter(this::matchZoneAffinityServer)
                .collect(Collectors.toList());
    }

    private boolean matchZoneAffinityServer(Server server){
        String zone = getZone();
        if(StringUtils.isEmpty(zone)){
            return true;
        }
        return StringUtils.equals(zone, server.getZone());
    }


    private String getZone(){
        if(StringUtils.isEmpty(zone)) {
            zone = ConfigurationManager.getDeploymentContext().getValue(DeploymentContext.ContextKey.zone);
        }
        return zone;
    }
}
