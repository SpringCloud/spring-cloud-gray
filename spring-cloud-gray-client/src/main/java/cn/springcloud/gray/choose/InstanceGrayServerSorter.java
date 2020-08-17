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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-08 20:15
 */
public class InstanceGrayServerSorter<SERVER> extends AbstractGrayServerSorter<SERVER> {

    private PolicyDecisionManager policyDecisionManager;

    public InstanceGrayServerSorter(
            ServerIdExtractor<SERVER> serverServerIdExtractor,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer<SERVER> serverExplainer) {

        super(grayManager, requestLocalStorage, serverServerIdExtractor, serverExplainer);
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs) {
        GrayService grayService = getGrayManager().getGrayService(serviceId);
        if (Objects.isNull(grayService) || !grayService.hasGrayInstance()) {
            return null;
        }
        List<ServerSpec<SERVER>> grayServers = new ArrayList<>(grayService.getGrayInstances().size());
        List<ServerSpec<SERVER>> normalServers = new ArrayList<>(Math.min(serverSpecs.size(), grayService.getGrayInstances().size()));

        for (ServerSpec<SERVER> serverSpec : serverSpecs) {
            if (Objects.nonNull(grayService.getGrayInstance(serverSpec.getInstanceId()))) {
                grayServers.add(serverSpec);
            } else {
                normalServers.add(serverSpec);
            }
        }

        return new ServerListResult<>(serviceId, grayServers, normalServers);
    }

    @Override
    protected List<ServerSpec<SERVER>> filterServerSpecAccordingToRoutePolicy(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs) {

        return serverSpecs.stream()
                .filter(this::matchGrayDecisions)
                .collect(Collectors.toList());
    }

    /**
     * 匹配策略
     *
     * @param serverSpec
     * @return
     */
    protected boolean matchGrayDecisions(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = createDecisionInputArgs(serverSpec);
        return matchGrayDecisions(decisionInputArgs);
    }


    protected boolean matchGrayDecisions(GrayDecisionInputArgs decisionInputArgs) {
        return policyDecisionManager.testPolicyPredicate(PredicateType.SERVER.name(), decisionInputArgs);
    }


}
