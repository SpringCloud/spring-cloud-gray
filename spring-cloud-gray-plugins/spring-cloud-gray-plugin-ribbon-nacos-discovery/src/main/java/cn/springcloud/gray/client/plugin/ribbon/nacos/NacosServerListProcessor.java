package cn.springcloud.gray.client.plugin.ribbon.nacos;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.servernode.AbstractServerListProcessor;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.loadbalancer.Server;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NacosServerListProcessor extends AbstractServerListProcessor<Server> {
    private static final Logger log = LoggerFactory.getLogger(NacosServerListProcessor.class);

    private NamingService namingService;

    public NacosServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties, NamingService namingService) {
        super(grayHoldoutServerProperties);
        this.namingService = namingService;
    }


    @Override
    protected List<Server> getServers(String serviceId, List<Server> servers) {
        List<InstanceStatus> statusList = getHoldoutInstanceStatus(serviceId);
        List<Server> holdoutServers = getInstances(serviceId).stream().filter(instance -> statusList.contains(getInstanceStatus(instance)))
                .map(NacosServer::new).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(holdoutServers)) {
            log.debug("没有找到 {} 服务处于{}状态的实例，返回原实例列表", serviceId, statusList);
            return servers;
        }
        List<Server> newServers = ListUtils.union(servers, holdoutServers);
        log.debug("通过破窗能力，{} 服务实例列表新增{}个实例，新列表:{}", serviceId, holdoutServers.size(), newServers);
        return newServers;
    }


    private InstanceStatus getInstanceStatus(Instance instance) {
        if (!instance.isEnabled()) {
            return InstanceStatus.DOWN;
        }
        if (!instance.isHealthy()) {
            return InstanceStatus.OUT_OF_SERVICE;
        }
        return InstanceStatus.UP;
    }


    private List<Instance> getInstances(String serviceId) {
        try {
            return namingService.getAllInstances(serviceId);
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
