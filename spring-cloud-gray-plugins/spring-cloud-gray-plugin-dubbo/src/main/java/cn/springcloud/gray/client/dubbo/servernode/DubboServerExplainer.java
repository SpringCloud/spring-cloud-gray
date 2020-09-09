package cn.springcloud.gray.client.dubbo.servernode;

import cn.springcloud.gray.AliasRegistry;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.servernode.VersionExtractor;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.rpc.Invoker;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-09-09 11:39
 */
public class DubboServerExplainer implements ServerExplainer<Invoker> {

    private VersionExtractor<Invoker> versionExtractor;
    private AliasRegistry aliasRegistry;


    @Override
    public VersionExtractor getVersionExtractor() {
        return versionExtractor;
    }


    @Override
    public ServerSpec<Invoker> apply(Invoker invoker) {

        return null;
    }

    @Override
    public String getServiceId(Invoker invoker) {
        URL providerURL = getProviderURL(invoker);
        return providerURL.getParameter(CommonConstants.APPLICATION_KEY);
    }

    @Override
    public String getInstaceId(Invoker invoker) {
        URL providerURL = getProviderURL(invoker);
        String protocol = providerURL.getProtocol();
        int port = providerURL.getPort();

        return null;
    }

    @Override
    public Map getMetadata(Invoker invoker) {
        String instanceId = getInstaceId(invoker);

        return null;
    }

    private URL getProviderURL(Invoker invoker) {
        return null;
    }
}
