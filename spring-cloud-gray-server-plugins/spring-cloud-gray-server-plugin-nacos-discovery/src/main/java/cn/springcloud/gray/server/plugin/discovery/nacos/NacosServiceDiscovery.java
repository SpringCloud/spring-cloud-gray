package cn.springcloud.gray.server.plugin.discovery.nacos;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.discovery.AbstractServiceDiscovery;
import cn.springcloud.gray.server.discovery.InstanceInfoAnalyser;
import cn.springcloud.gray.server.discovery.ServiceInfo;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NacosServiceDiscovery extends AbstractServiceDiscovery<Instance> {

    private static final Logger log = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private NacosDiscoveryProperties discoveryProperties;

    public NacosServiceDiscovery(NacosDiscoveryProperties discoveryProperties) {
        this(discoveryProperties, Collections.EMPTY_LIST);
    }

    public NacosServiceDiscovery(
            NacosDiscoveryProperties discoveryProperties,
            List<InstanceInfoAnalyser<Instance>> instanceInfoAnalysers) {
        this.discoveryProperties = discoveryProperties;
        this.setInstanceInfoAnalysers(instanceInfoAnalysers);
    }

    @Override
    public List<ServiceInfo> listAllSerivceInfos() {
        return getServices().stream().map(service -> new ServiceInfo(service)).collect(Collectors.toList());

    }

    @Override
    public ServiceInfo getServiceInfo(String serviceId) {
        if (!getServices().contains(serviceId)) {
            return null;
        }

        return new ServiceInfo(serviceId);
    }

    @Override
    public List<InstanceInfo> listInstanceInfos(String serviceId) {
        List<Instance> instances = null;
        try {
            instances = discoveryProperties.namingServiceInstance().getAllInstances(serviceId, false);
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
            return ListUtils.EMPTY_LIST;
        }
        return instances.stream().map(this::createInstanceInfo).collect(Collectors.toList());
    }

    @Override
    public InstanceInfo getInstanceInfo(String serviceId, String instanceId) {
        return getInstanceInfos(serviceId).get(instanceId);
    }

    private List<String> getServices() {
        try {
            return discoveryProperties.namingServiceInstance()
                    .getServicesOfServer(1, Integer.MAX_VALUE).getData();
        } catch (NacosException e) {
            return Collections.emptyList();
        }
    }

    @Override
    protected InstanceInfo createInstanceInfo(Instance instance) {
        InstanceStatus instanceStatus = InstanceStatus.DOWN;
        if (instance.isEnabled()) {
            if (instance.isHealthy()) {
                instanceStatus = InstanceStatus.UP;
            } else {
                instanceStatus = InstanceStatus.OUT_OF_SERVICE;
            }
        }

        return InstanceInfo.builder()
                .serviceId(instance.getServiceName())
                .instanceId(instance.getInstanceId())
                .host(instance.getIp())
                .port(instance.getPort())
                .instanceStatus(instanceStatus)
                .build();
    }

}
