package cn.springcloud.gray.choose;

import cn.springcloud.gray.DataSet;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-09 03:49
 */
@Slf4j
public class ServiceGrayServerSorter<SERVER> extends AbstractGrayServerSorter<SERVER> {

    private PolicyDecisionManager policyDecisionManager;

    public ServiceGrayServerSorter(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerIdExtractor<SERVER> serverServerIdExtractor,
            ServerExplainer<SERVER> serverExplainer) {
        super(grayManager, requestLocalStorage, serverServerIdExtractor, serverExplainer);
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    protected ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(String serviceId, List<ServerSpec<SERVER>> serverSpecs) {
        List<ServerSpec<SERVER>> serverSpecList = serverSpecs;
        GrayManager grayManager = getGrayManager();
        GrayService grayService = grayManager.getGrayService(serviceId);
        if (Objects.nonNull(grayService) && !grayService.getRoutePolicies().isEmpty()) {
            serverSpecList = filterServiceGrayPolicies(grayService.getRoutePolicies().getDatas(), serverSpecs);
        }

        Collection<String> multiVersions = grayService.getMultiVersionRotePolicies().keySet();
        if (CollectionUtils.isNotEmpty(multiVersions)) {
            return new ServerListResult<>(serviceId, Collections.EMPTY_LIST, serverSpecList);
        }

        List<ServerSpec<SERVER>> grayServerSpecs = new ArrayList<>(serverSpecList.size());
        List<ServerSpec<SERVER>> normalServerSpecs = new ArrayList<>(serverSpecList.size());
        serverSpecList.forEach(serverSpec -> {
            if (multiVersions.contains(serverSpec.getVersion())) {
                grayServerSpecs.add(serverSpec);
            } else {
                normalServerSpecs.add(serverSpec);
            }
        });
        return new ServerListResult<>(serviceId, grayServerSpecs, normalServerSpecs);
    }

    @Override
    protected List<ServerSpec<SERVER>> filterServerSpecAccordingToRoutePolicy(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs) {
        if (CollectionUtils.isEmpty(serverSpecs)) {
            return serverSpecs;
        }

        PolicyPredicate policyPredicate =
                policyDecisionManager.getPolicyPredicate(PredicateType.SERVICE_MULTI_VERSION_SERVER.name());
        if (Objects.isNull(policyPredicate)) {
            log.error("没有找到灰度策略断言器, predicateType:{}, service级的筛选跳过",
                    PredicateType.SERVICE_MULTI_VERSION_SERVER.name());
            return serverSpecs;
        }

        GrayService grayService = getGrayManager().getGrayService(serviceId);
        if (Objects.isNull(grayService)) {
            return serverSpecs;
        }
        return filterServerSpecAccordingToRoutePolicy(grayService, policyPredicate, serverSpecs);
    }


    /**
     * 断言service 多版本灰度策略，并过滤不匹配的server返回。
     *
     * @param grayService
     * @param policyPredicate
     * @param serverSpecs
     * @return
     */
    private List<ServerSpec<SERVER>> filterServerSpecAccordingToRoutePolicy(
            GrayService grayService, PolicyPredicate policyPredicate, List<ServerSpec<SERVER>> serverSpecs) {
        Map<String, DataSet<String>> multiVersionRoutePoliciesMap = grayService.getMultiVersionRotePolicies();
        Map<String, List<Policy>> multiVersionPolicies = new HashMap<>();
        return serverSpecs.stream().filter(serverSpec -> {
            String version = serverSpec.getVersion();
            List<Policy> policies = multiVersionPolicies.get(version);
            if (Objects.isNull(policies)) {
                policies = Collections.EMPTY_LIST;
                DataSet<String> routePolicies = multiVersionRoutePoliciesMap.get(version);
                if (Objects.nonNull(routePolicies)) {
                    policies = policyDecisionManager.getPolicies(routePolicies.getDatas());
                }
                multiVersionPolicies.put(version, policies);
            }

            return CollectionUtils.isEmpty(policies)
                    || policyPredicate.testPolicies(policies, createDecisionInputArgs(serverSpec));
        }).collect(Collectors.toList());
    }


    /**
     * 断言service级灰度策略，并过滤不匹配的server返回。
     * 如果灰度策略和策略断言器任何一个为空，跳过service级的筛选过滤。
     *
     * @param serverSpecs
     * @param grayPolicies
     * @return
     */
    private List<ServerSpec<SERVER>> filterServiceGrayPolicies(
            Collection<String> grayPolicies, List<ServerSpec<SERVER>> serverSpecs) {

        PolicyPredicate policyPredicate = policyDecisionManager.getPolicyPredicate(PredicateType.SERVICE_SERVER.name());
        if (Objects.isNull(policyPredicate)) {
            log.error("没有找到灰度策略断言器, predicateType:{}, service级的筛选跳过", PredicateType.SERVICE_SERVER.name());
            return serverSpecs;
        }

        List<Policy> policies = policyDecisionManager.getPolicies(grayPolicies);
        if (CollectionUtils.isEmpty(policies)) {
            log.error("没有找到灰度的策略, policyIds:{}, service级的筛选跳过", grayPolicies);
            return serverSpecs;
        }


        return filterServiceGrayPolicies(policyPredicate, policies, serverSpecs);
    }


    /**
     * 断言service级灰度策略，并过滤不匹配的server返回。
     *
     * @param policyPredicate
     * @param policies
     * @param serverSpecs
     * @return
     */
    private List<ServerSpec<SERVER>> filterServiceGrayPolicies(
            PolicyPredicate policyPredicate, Collection<Policy> policies, Collection<ServerSpec<SERVER>> serverSpecs) {
        return serverSpecs.stream().filter(serverSpec -> {
            GrayDecisionInputArgs decisionInputArgs = createDecisionInputArgs(serverSpec);
            return policyPredicate.testPolicies(policies, decisionInputArgs);
        }).collect(Collectors.toList());
    }


}
