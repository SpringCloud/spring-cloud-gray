package cn.springcloud.gray.servernode;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.model.InstanceStatus;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public abstract class AbstractServerListProcessor<S> implements ServerListProcessor<S> {

    protected GrayHoldoutServerProperties grayHoldoutServerProperties;

    public AbstractServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties) {
        this.grayHoldoutServerProperties = grayHoldoutServerProperties;
    }

    @Override
    public List<S> process(String serviceId, List<S> servers) {
        if (!grayHoldoutServerProperties.isEnabled() || CollectionUtils.isEmpty(getHoldoutInstanceStatus(serviceId))) {
            return servers;
        }

        return getServers(serviceId, servers);
    }

    protected List<InstanceStatus> getHoldoutInstanceStatus(String serviceId){
        return grayHoldoutServerProperties.getServices().get(serviceId);
    }

    protected abstract List<S> getServers(String serviceId, List<S> servers);
}
