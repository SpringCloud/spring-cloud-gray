package cn.springcloud.gray.client.dubbo.servernode;

import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.servernode.VersionExtractor;
import cn.springcloud.gray.utils.StringUtils;
import org.apache.dubbo.rpc.Invoker;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-09-09 11:39
 */
public class DubboServerExplainer implements ServerExplainer<Invoker> {

    private VersionExtractor<Invoker> versionExtractor;
    private ServerMetadataExtractor<Invoker> serverMetadataExtractor;
    private ServiceInstanceExtractor serviceInstanceExtractor;


    public DubboServerExplainer(
            VersionExtractor<Invoker> versionExtractor,
            ServerMetadataExtractor<Invoker> serverMetadataExtractor,
            ServiceInstanceExtractor serviceInstanceExtractor) {
        this.versionExtractor = versionExtractor;
        this.serverMetadataExtractor = serverMetadataExtractor;
        this.serviceInstanceExtractor = serviceInstanceExtractor;
    }

    @Override
    public ServerSpec<Invoker> apply(Invoker invoker) {
        String serviceId = getServiceId(invoker);
        String instanceId = getInstaceId(invoker);
        Map metadata = getMetadata(invoker, serviceId, instanceId);
        return ServerSpec.<Invoker>builder()
                .server(invoker)
                .instanceId(instanceId)
                .serviceId(serviceId)
                .metadata(metadata)
                .version(getVersion(invoker, metadata))
                .build();
    }

    @Override
    public VersionExtractor getVersionExtractor() {
        return versionExtractor;
    }


    @Override
    public String getServiceId(Invoker invoker) {
        return InvokerExplainHelper.getServiceId(invoker);
    }

    @Override
    public String getInstaceId(Invoker invoker) {
        String alias = InvokerExplainHelper.getAlias(invoker);
        String instanceId = serviceInstanceExtractor.getServiceInstanceId(getServiceId(invoker), alias);
        return StringUtils.isEmpty(instanceId) ? alias : instanceId;
    }

    @Override
    public Map getMetadata(Invoker invoker) {
        String serviceId = getServiceId(invoker);
        String instanceId = getInstaceId(invoker);
        return getMetadata(invoker, serviceId, instanceId);
    }


    private Map<String, String> getMetadata(Invoker invoker, String serviceId, String instanceId) {
        return serverMetadataExtractor.getMetadata(invoker, serviceId, instanceId);
    }


}
