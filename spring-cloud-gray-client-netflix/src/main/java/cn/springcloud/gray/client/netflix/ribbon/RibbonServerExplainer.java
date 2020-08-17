package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.VersionExtractor;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.Map;

public class RibbonServerExplainer implements ServerExplainer<Server> {

    private SpringClientFactory springClientFactory;
    private VersionExtractor<Server> versionExtractor;

    public RibbonServerExplainer(SpringClientFactory springClientFactory, VersionExtractor<Server> versionExtractor) {
        this.springClientFactory = springClientFactory;
        this.versionExtractor = versionExtractor;
    }

    @Override
    public VersionExtractor getVersionExtractor() {
        return versionExtractor;
    }

    @Override
    public String getServiceId(Server server) {
        return server.getMetaInfo().getServiceIdForDiscovery();
    }

    @Override
    public String getInstaceId(Server server) {
        return server.getMetaInfo().getInstanceId();
    }

    @Override
    public Map getMetadata(Server server) {
        return getServerMetadata(server.getMetaInfo().getServiceIdForDiscovery(), server);
    }

    public ServerIntrospector serverIntrospector(String serviceId) {
        if (springClientFactory == null) {
            return new DefaultServerIntrospector();
        }
        ServerIntrospector serverIntrospector = this.springClientFactory.getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

    /**
     * 获取实例的metadata信息
     *
     * @param serviceId 服务id
     * @param server    ribbon服务器(服务实例)
     * @return 服务实例的metadata信息
     */
    public Map<String, String> getServerMetadata(String serviceId, Server server) {
        return serverIntrospector(serviceId).getMetadata(server);
    }
}
