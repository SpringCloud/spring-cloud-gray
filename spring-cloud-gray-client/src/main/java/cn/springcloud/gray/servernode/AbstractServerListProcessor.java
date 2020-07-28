package cn.springcloud.gray.servernode;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.model.InstanceStatus;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractServerListProcessor<S> implements ServerListProcessor<S> {

    private static Logger log = LoggerFactory.getLogger(AbstractServerListProcessor.class);

    protected GrayHoldoutServerProperties grayHoldoutServerProperties;

    public AbstractServerListProcessor(GrayHoldoutServerProperties grayHoldoutServerProperties) {
        this.grayHoldoutServerProperties = grayHoldoutServerProperties;
    }

    @Override
    public List<S> process(String serviceId, List<S> servers) {
        if (!grayHoldoutServerProperties.isEnabled()) {
            log.debug("破窗开关未打开，返回原实例列表");
            return servers;
        }

        if (CollectionUtils.isEmpty(getHoldoutInstanceStatus(serviceId))) {
            log.debug("未设置服务 {} 的破窗实例状态，返回原实例列表", serviceId);
            return servers;
        }

        return getServers(serviceId, servers);
    }

    protected List<InstanceStatus> getHoldoutInstanceStatus(String serviceId) {
        return grayHoldoutServerProperties.getServices().get(serviceId);
    }

    protected abstract List<S> getServers(String serviceId, List<S> servers);
}
