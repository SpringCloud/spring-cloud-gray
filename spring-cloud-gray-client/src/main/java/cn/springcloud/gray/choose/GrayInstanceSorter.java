package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-08 20:15
 */
public class GrayInstanceSorter<SERVER> extends AbstractGraySorter<SERVER> {

    private GrayManager grayManager;

    private RequestLocalStorage requestLocalStorage;

    private PolicyDecisionManager policyDecisionManager;

    private ServerExplainer<SERVER> serverExplainer;

    protected ServerListProcessor serverListProcessor;

    public GrayInstanceSorter(
            ServerIdExtractor<SERVER> serverServerIdExtractor,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer<SERVER> serverExplainer,
            ServerListProcessor serverListProcessor) {
        super(serverServerIdExtractor);
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.policyDecisionManager = policyDecisionManager;
        this.serverExplainer = serverExplainer;
        this.serverListProcessor = serverListProcessor;
    }


    @Override
    public ServerListResult<SERVER> distinguishServerList(List<SERVER> servers) {
        String serviceId = getServiceId(servers);
        if (StringUtils.isEmpty(serviceId)) {
            return null;
        }
        if (!grayManager.hasGray(serviceId)) {
            return null;
        }

        GrayService grayService = grayManager.getGrayService(serviceId);
        List<SERVER> serverList = serverListProcessor.process(serviceId, servers);
        List<SERVER> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
        List<SERVER> normalServers = new ArrayList<>(Math.min(servers.size(), grayService.getGrayInstances().size()));

        for (SERVER server : serverList) {
            String instanceId = serverExplainer.getInstaceId(server);
            if (grayService.getGrayInstance(instanceId) != null) {
                grayServers.add(server);
            } else {
                normalServers.add(server);
            }
        }

        return new ServerListResult<>(serviceId, grayServers, normalServers);
    }

    @Override
    public ServerListResult<SERVER> distinguishAndMatchGrayServerList(List<SERVER> servers) {
        ServerListResult<SERVER> serverListResult = distinguishServerList(servers);
        if (serverListResult == null) {
            return null;
        }

        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            List<SERVER> matchedGrayServers = serverListResult.getGrayServers()
                    .stream()
                    .filter(this::matchGrayDecisions)
                    .collect(Collectors.toList());
            serverListResult.setGrayServers(matchedGrayServers);
        } else {
            serverListResult.setGrayServers(ListUtils.EMPTY_LIST);
        }

        return serverListResult;
    }

    protected boolean matchGrayDecisions(SERVER server) {
        return matchGrayDecisions(serverExplainer.apply(server));
    }

    protected boolean matchGrayDecisions(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = new GrayDecisionInputArgs();
        decisionInputArgs.setServer(serverSpec);
        decisionInputArgs.setGrayRequest(requestLocalStorage.getGrayRequest());

        return policyDecisionManager.testPolicyPredicate(PredicateType.SERVER.name(), decisionInputArgs);
    }


}
