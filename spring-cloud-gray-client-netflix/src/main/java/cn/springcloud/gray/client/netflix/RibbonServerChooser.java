package cn.springcloud.gray.client.netflix;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerChooser;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.choose.GrayPredicate;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RibbonServerChooser implements ServerChooser<Server> {

    private GrayManager grayManager;

    private RequestLocalStorage requestLocalStorage;

    private GrayPredicate grayPredicate;

    private ServerExplainer<Server> serverExplainer;

    protected ServerListProcessor serverListProcessor;


    public RibbonServerChooser(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            GrayPredicate grayPredicate,
            ServerExplainer<Server> serverExplainer,
            ServerListProcessor serverListProcessor) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.grayPredicate = grayPredicate;
        this.serverExplainer = serverExplainer;
        this.serverListProcessor = serverListProcessor;
    }

    @Override
    public boolean matchGrayDecisions(ServerSpec serverSpec) {
        return grayPredicate.apply(serverSpec);
    }

    @Override
    public boolean matchGrayDecisions(Server server) {
        return matchGrayDecisions(serverExplainer.apply(server));
    }

    @Override
    public ServerListResult<Server> distinguishServerList(List<Server> servers) {
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        if (grayRequest == null) {
            return null;
        }

        return distinguishServerList(grayRequest.getServiceId(), servers);
    }


    @Override
    public ServerListResult<Server> distinguishAndMatchGrayServerList(List<Server> servers) {
        ServerListResult<Server> serverListResult = distinguishServerList(servers);
        if (serverListResult == null) {
            return null;
        }

        serverListResult.setGrayServers(
                serverListResult.getGrayServers().stream()
                        .filter(this::matchGrayDecisions)
                        .collect(Collectors.toList()));

        return serverListResult;
    }


    private ServerListResult<Server> distinguishServerList(String serviceId, List<Server> servers) {
        if (!grayManager.hasGray(serviceId)) {
            return null;
        }

        GrayService grayService = grayManager.getGrayService(serviceId);
        List<Server> serverList = serverListProcessor.process(serviceId, servers);
        List<Server> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
        List<Server> normalServers = new ArrayList<>(Math.min(servers.size(), grayService.getGrayInstances().size()));

        for (Server server : serverList) {
            if (grayService.getGrayInstance(server.getMetaInfo().getInstanceId()) != null) {
                grayServers.add(server);
            } else {
                normalServers.add(server);
            }
        }

        return new ServerListResult<>(serviceId, grayServers, normalServers);
    }
}
