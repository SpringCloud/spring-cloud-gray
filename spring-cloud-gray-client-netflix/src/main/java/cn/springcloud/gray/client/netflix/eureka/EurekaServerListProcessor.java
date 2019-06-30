package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.ServerListProcessor;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.discovery.shared.Application;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EurekaServerListProcessor implements ServerListProcessor<Server>, EurekaEventListener {

    private static final Logger log = LoggerFactory.getLogger(EurekaServerListProcessor.class);

    private EurekaClient eurekaClient;
    private GrayHoldoutServerProperties grayHoldoutServerProperties;


    private Map<String, List<Server>> unUpServersMap = new HashMap<>();
    private Map<String, List<Server>> serversMap = new ConcurrentHashMap<>();


    public EurekaServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.grayHoldoutServerProperties = grayHoldoutServerProperties;

        reloadUpServersMap();
        eurekaClient.registerEventListener(this);
    }

    @Override
    public List<Server> process(String serviceId, List<Server> servers) {
        if (!grayHoldoutServerProperties.isEnabled() || !grayHoldoutServerProperties.getServices().containsKey(serviceId)) {
            return servers;
        }
        List<Server> serverList = serversMap.get(serviceId);
        if (!CollectionUtils.isEmpty(serverList)) {
            return serverList;
        }

        Application application = eurekaClient.getApplication(serviceId);
        if (Objects.isNull(application)) {
            return servers;
        }

        List<Server> unUpServers = getUnUpServers(serviceId);
        if (CollectionUtils.isEmpty(unUpServers)) {
            serverList = servers;
        } else {
            serverList = ListUtils.union(servers, unUpServers);
        }
        serversMap.put(serviceId, serverList);
        return serverList;
    }


    @Override
    public void onEvent(EurekaEvent event) {
        log.debug("接收到eureka事件:{}, 刷新缓存的server list", event);
        reloadUpServersMap();
        serversMap.clear();
    }

    private List<Server> getUnUpServers(String serviceId) {
        return unUpServersMap.get(serviceId);
    }

    private List<Server> getUnUpServerList(String serviceId, List<InstanceStatus> instanceStatuses) {
        Application application = eurekaClient.getApplication(serviceId);
        if (Objects.isNull(application)) {
            return null;
        }
        return application.getInstancesAsIsFromEureka().stream()
                .filter(instanceInfo -> instanceStatuses.contains(EurekaInstatnceTransformer.toGrayInstanceStatus(instanceInfo.getStatus())))
                .map(instanceInfo -> new DiscoveryEnabledServer(instanceInfo, false))
                .collect(Collectors.toList());
    }

    private void reloadUpServersMap() {
        Map<String, List<Server>> unUpServersMap = new HashMap<>();
        grayHoldoutServerProperties.getServices().forEach(
                (serviceId, instanceStatuses) -> {
                    if (CollectionUtils.isEmpty(instanceStatuses)) {
                        return;
                    }
                    List<Server> unUpServers = getUnUpServerList(serviceId, instanceStatuses);
                    if (CollectionUtils.isNotEmpty(unUpServers)) {
                        unUpServersMap.put(serviceId, unUpServers);
                    }
                });

        this.unUpServersMap = unUpServersMap;
    }

}
