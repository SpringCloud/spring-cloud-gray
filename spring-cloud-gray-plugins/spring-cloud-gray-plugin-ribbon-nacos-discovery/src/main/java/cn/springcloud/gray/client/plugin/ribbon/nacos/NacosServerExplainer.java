package cn.springcloud.gray.client.plugin.ribbon.nacos;

import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.servernode.VersionExtractor;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.Map;

public class NacosServerExplainer implements ServerExplainer<Server> {

    private SpringClientFactory springClientFactory;
    private VersionExtractor<Server> versionExtractor;

    public NacosServerExplainer(SpringClientFactory springClientFactory, VersionExtractor<Server> versionExtractor) {
        this.springClientFactory = springClientFactory;
        this.versionExtractor = versionExtractor;
    }

    @Override
    public ServerSpec<Server> apply(Server server) {
        String seviceId = getInstaceId(server);
        Map metadata = getServerMetadata(seviceId, server);
        return ServerSpec.<Server>builder()
                .server(server)
                .instanceId(server.getMetaInfo().getInstanceId())
                .serviceId(seviceId)
                .metadata(metadata)
                .build();
    }

    @Override
    public VersionExtractor getVersionExtractor() {
        return versionExtractor;
    }

    @Override
    public String getServiceId(Server server) {
        String appName = server.getMetaInfo().getAppName();
        String seviceId = appName;
        if (StringUtils.contains(appName, "@@")) {
            seviceId = StringUtils.split(appName, "@@")[1];
        }
        return seviceId;
    }

    @Override
    public String getInstaceId(Server server) {
        return server.getMetaInfo().getInstanceId();
    }

    @Override
    public Map getMetadata(Server server) {
        return getServerMetadata(getServiceId(server), server);
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
