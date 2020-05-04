package cn.springcloud.gray.client.netflix;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerChooser;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.choose.PredicateType;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import com.netflix.loadbalancer.Server;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RibbonServerChooser implements ServerChooser<Server> {

    private GrayManager grayManager;

    private RequestLocalStorage requestLocalStorage;

    private PolicyDecisionManager policyDecisionManager;

    private ServerExplainer<Server> serverExplainer;

    protected ServerListProcessor serverListProcessor;


    public RibbonServerChooser(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer<Server> serverExplainer,
            ServerListProcessor serverListProcessor) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.policyDecisionManager = policyDecisionManager;
        this.serverExplainer = serverExplainer;
        this.serverListProcessor = serverListProcessor;
    }

    @Override
    public boolean matchGrayDecisions(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = new GrayDecisionInputArgs();
        decisionInputArgs.setServer(serverSpec);
        decisionInputArgs.setGrayRequest(requestLocalStorage.getGrayRequest());

        return policyDecisionManager.testPolicyPredicate(PredicateType.SERVER.name(), decisionInputArgs);
    }

    @Override
    public boolean matchGrayDecisions(Server server) {
        return matchGrayDecisions(serverExplainer.apply(server));
    }

    @Override
    public ServerListResult<Server> distinguishServerList(List<Server> servers) {
        String serviceId = getServiceId(servers);
        if (StringUtils.isEmpty(serviceId)) {
            return null;
        }
        return distinguishServerList(serviceId, servers);
    }


    @Override
    public ServerListResult<Server> distinguishAndMatchGrayServerList(List<Server> servers) {
        ServerListResult<Server> serverListResult = distinguishServerList(servers);
        if (serverListResult == null) {
            return null;
        }

        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            serverListResult.setGrayServers(
                    serverListResult.getGrayServers().stream()
                            .filter(this::matchGrayDecisions)
                            .collect(Collectors.toList()));
        } else {
            serverListResult.setGrayServers(ListUtils.EMPTY_LIST);
        }

        return serverListResult;
    }

    private String getServiceId(List<Server> servers) {
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        if (grayRequest != null && StringUtils.isNotEmpty(grayRequest.getServiceId())) {
            return grayRequest.getServiceId();
        }
        if (CollectionUtils.isNotEmpty(servers)) {
            Server server = servers.get(0);
            if (!Objects.isNull(server)) {
                return server.getMetaInfo().getServiceIdForDiscovery();
            }
        }
        return null;
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
