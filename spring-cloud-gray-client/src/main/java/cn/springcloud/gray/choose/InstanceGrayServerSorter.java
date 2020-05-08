package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-05-08 20:15
 */
public class InstanceGrayServerSorter<SERVER> extends AbstractGrayServerSorter<SERVER> {

    private RequestLocalStorage requestLocalStorage;

    private PolicyDecisionManager policyDecisionManager;

    public InstanceGrayServerSorter(
            ServerIdExtractor<SERVER> serverServerIdExtractor,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer<SERVER> serverExplainer) {

        super(grayManager, serverServerIdExtractor, serverExplainer);
        this.requestLocalStorage = requestLocalStorage;
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs) {
        GrayService grayService = getGrayManager().getGrayService(serviceId);
        List<ServerSpec<SERVER>> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
        List<ServerSpec<SERVER>> normalServers = new ArrayList<>(Math.min(serverSpecs.size(), grayService.getGrayInstances().size()));

        for (ServerSpec<SERVER> serverSpec : serverSpecs) {
            if (grayService.getGrayInstance(serverSpec.getInstanceId()) != null) {
                grayServers.add(serverSpec);
            } else {
                normalServers.add(serverSpec);
            }
        }

        return new ServerListResult<>(serviceId, grayServers, normalServers);
    }

    @Override
    protected boolean matchGrayDecisions(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = new GrayDecisionInputArgs();
        decisionInputArgs.setServer(serverSpec);
        decisionInputArgs.setGrayRequest(requestLocalStorage.getGrayRequest());

        return policyDecisionManager.testPolicyPredicate(PredicateType.SERVER.name(), decisionInputArgs);
    }


}
