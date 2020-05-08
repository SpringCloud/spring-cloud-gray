package cn.springcloud.gray.client.plugin.ribbon.nacos;

import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerSpec;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.Map;

public class NacosServerExplainer implements ServerExplainer<Server> {

    private SpringClientFactory springClientFactory;

    public NacosServerExplainer(SpringClientFactory springClientFactory) {
        this.springClientFactory = springClientFactory;
    }

    @Override
    public ServerSpec apply(Server server) {
        String seviceId = getInstaceId(server);
        Map metadata = getServerMetadata(seviceId, server);
        return ServerSpec.builder().instanceId(server.getMetaInfo().getInstanceId())
                .serviceId(seviceId)
                .metadatas(metadata).build();
    }

    @Override
    public String getServiceId(Server server) {
        return server.getMetaInfo().getInstanceId();
    }

    @Override
    public String getInstaceId(Server server) {
        String appName = server.getMetaInfo().getAppName();
        String seviceId = appName;
        if (StringUtils.contains(appName, "@@")) {
            seviceId = StringUtils.split(appName, "@@")[1];
        }
        return seviceId;
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
