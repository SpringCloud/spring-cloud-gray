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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class EurekaServerListProcessor implements ServerListProcessor<Server>, EurekaEventListener, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(EurekaServerListProcessor.class);

    private EurekaClient eurekaClient;
    private GrayHoldoutServerProperties grayHoldoutServerProperties;
    private Semaphore semaphore = new Semaphore(1);


    private volatile Map<String, List<Server>> unUpServersMap = new HashMap<>();
    private Map<String, List<Server>> serversMap = new ConcurrentHashMap<>();


    public EurekaServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.grayHoldoutServerProperties = grayHoldoutServerProperties;
    }

    @Override
    public List<Server> process(String serviceId, List<Server> servers) {
        if (!grayHoldoutServerProperties.isEnabled() || CollectionUtils.isEmpty(grayHoldoutServerProperties.getServices().get(serviceId))) {
            return servers;
        }

        List<Server> serverList = null;

        if(grayHoldoutServerProperties.isCacheable()) {
            serverList = serversMap.get(serviceId);
            if (CollectionUtils.isNotEmpty(serverList)) {
                return serverList;
            }
        }

        serverList = servers;
        List<Server> unUpServers = getUnUpServers(serviceId);
        if (CollectionUtils.isNotEmpty(unUpServers)) {
            serverList = ListUtils.union(servers, unUpServers);
        }
        if(grayHoldoutServerProperties.isCacheable()){
            serversMap.put(serviceId, serverList);
        }
        return serverList;
    }


    @Override
    public void onEvent(EurekaEvent event) {
        log.debug("接收到eureka事件:{}, 刷新缓存的server list", event);
        reload();
    }

    private List<Server> getUnUpServers(String serviceId) {
        return unUpServersMap.get(serviceId);
    }

    protected List<Server> getUnUpServerList(String serviceId, List<InstanceStatus> instanceStatuses) {

        Application application = eurekaClient.getApplication(serviceId);
        if (Objects.isNull(application)) {
            return null;
        }
        return application.getInstancesAsIsFromEureka().stream()
                .filter(instanceInfo -> instanceStatuses.contains(EurekaInstatnceTransformer.toGrayInstanceStatus(instanceInfo.getStatus())))
                .map(instanceInfo -> {
                    DiscoveryEnabledServer server = new DiscoveryEnabledServer(instanceInfo, false);
                    String zone = server.getInstanceInfo().getMetadata().get("zone");
                    if (StringUtils.isNotEmpty(zone)) {
                        server.setZone(zone);
                    }
                    return server;
                })
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

    @Override
    public void afterPropertiesSet() throws Exception {
        reloadAndRegister();
    }


    @Order
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        reloadAndRegister();
    }

    public void reload() {
        if (!semaphore.tryAcquire()) {
            log.info("已有其它线程在执行reload");
            return;
        }
        try {
            reloadUpServersMap();
            serversMap.clear();
        } finally {
            semaphore.release();
        }
    }

    public void reloadAndRegister() {
        try {
            reload();
        } finally {
            eurekaClient.registerEventListener(this);
        }
    }

}
