package cn.springcloud.gray.mock;

import cn.springcloud.gray.Cache;
import cn.springcloud.gray.Pair;
import cn.springcloud.gray.choose.PolicyPredicate;
import cn.springcloud.gray.choose.PredicateType;
import cn.springcloud.gray.choose.SimplePolicyPredicate;
import cn.springcloud.gray.constants.PublicConstants;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.handle.HandleInfo;
import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.handle.HandleRuleInfo;
import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springcloud.gray.model.HandleType;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.utils.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-24 23:49
 */
public class DefaultMockManager implements MockManager {

    private HandleManager handleManager;
    private HandleRuleManager handleRuleManager;
    private MockDriver mockDriver;
    private RequestLocalStorage requestLocalStorage;
    private PolicyDecisionManager policyDecisionManager;
    private ServerExplainer serverExplainer;
    private Cache<String, MockHandle> handleCache;


    public DefaultMockManager(
            HandleManager handleManager,
            HandleRuleManager handleRuleManager,
            MockDriver mockDriver,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer serverExplainer,
            Cache<String, MockHandle> handleCache) {
        this.handleManager = handleManager;
        this.handleRuleManager = handleRuleManager;
        this.mockDriver = mockDriver;
        this.requestLocalStorage = requestLocalStorage;
        this.policyDecisionManager = policyDecisionManager;
        this.serverExplainer = serverExplainer;
        this.handleCache = handleCache;
    }

    @Override
    public boolean isEnableMock() {
        return true;
    }

    @Override
    public boolean isEnableMock(String mockRuleType) {
        return true;
    }

    @Override
    public <RESULT> MockHandle<RESULT> getMockHandle(String id) {
        return handleCache.get(id, this::createMockHandle);
    }


    @Override
    public void invalidateCache(String id) {
        handleCache.invalidate(id);
    }

    @Override
    public void invalidateAllCache() {
        handleCache.invalidateAll();
    }

    @Override
    public String predicateMockHandleId(String mockRuleType) {
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        if (Objects.isNull(grayRequest)) {
            return null;
        }
        return predicateMockHandleId(mockRuleType, grayRequest);
    }

    @Override
    public String predicateMockHandleId(String mockRuleType, GrayRequest grayRequest) {
        List<HandleRuleInfo> handleRuleInfos = handleRuleManager.getHandleRuleTypeInfos(mockRuleType);
        if (Objects.isNull(handleRuleInfos)) {
            return null;
        }
        PolicyPredicate policyPredicate = getPolicyPredicate();
        if (Objects.isNull(policyPredicate)) {
            return null;
        }

        GrayDecisionInputArgs decisionInputArgs = new GrayDecisionInputArgs();
        decisionInputArgs.setGrayRequest(grayRequest);
        Object server = grayRequest.getAttachment(PublicConstants.CHOOSED_SERVER);
        if (Objects.nonNull(server)) {
            ServerSpec serverSpec = serverExplainer.apply(server);
            decisionInputArgs.setServer(serverSpec);
        }

        for (HandleRuleInfo handleRuleInfo : handleRuleInfos) {
            List<Policy> policies = getPolicies(handleRuleInfo.getMatchingPolicies().getDatas());
            if (policyPredicate.testPolicies(policies, decisionInputArgs)
                    && StringUtils.isNotEmpty(handleRuleInfo.getHandleOption())) {
                return handleRuleInfo.getHandleOption();
            }
        }

        return null;
    }

    @Override
    public <RESULT> RESULT invokeMockHandle(String mockHandleId) {
        GrayReuqestMockInfo grayReuqestMockInfo = createGrayReuqestMockInfo();
        if (Objects.isNull(grayReuqestMockInfo)) {
            return null;
        }
        return invokeMockHandle(mockHandleId, grayReuqestMockInfo);

    }

    @Override
    public <RESULT> RESULT invokeMockHandle(String mockHandleId, GrayReuqestMockInfo mockInfo) {
        MockHandle<RESULT> mockHandle = getMockHandle(mockHandleId);
        if (Objects.isNull(mockHandle)) {
            return null;
        }
        return mockHandle.mock(mockInfo);
    }

    @Override
    public <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType) {
        GrayReuqestMockInfo grayReuqestMockInfo = createGrayReuqestMockInfo();
        if (Objects.isNull(grayReuqestMockInfo)) {
            return null;
        }
        return predicateAndExcuteMockHandle(mockRuleType, grayReuqestMockInfo);
    }

    @Override
    public <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType, GrayReuqestMockInfo mockInfo) {
        GrayRequest grayRequest = mockInfo.getGrayRequest();
        if (Objects.isNull(grayRequest)) {
            return null;
        }
        String mockHandleId = predicateMockHandleId(mockRuleType, grayRequest);
        if (StringUtils.isEmpty(mockHandleId)) {
            return null;
        }
        return invokeMockHandle(mockHandleId, mockInfo);
    }


    protected <RESULT> MockHandle<RESULT> createMockHandle(String id) {
        HandleInfo handleInfo = handleManager.getHandleInfo(id);
        if (Objects.isNull(handleInfo) || !Objects.equals(handleInfo.getType(), HandleType.MOCK.code())) {
            return null;
        }
        List<Pair<String, Object>> mockActionDefinitions = handleInfo.getHandleActionDefinitions()
                .stream()
                .map(actionDefinition -> new Pair<String, Object>(actionDefinition.getActionName(), actionDefinition.getInfos()))
                .collect(Collectors.toList());
        MockHandle<RESULT> mockHandle = mockDriver.createMockHandle(mockActionDefinitions);
        return mockHandle;
    }


    protected GrayReuqestMockInfo createGrayReuqestMockInfo() {
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        if (Objects.isNull(grayRequest)) {
            return null;
        }
        return GrayReuqestMockInfo.builder()
                .grayRequest(grayRequest)
                .build();
    }

    protected PolicyPredicate getPolicyPredicate() {
        PolicyPredicate policyPredicate = policyDecisionManager.getPolicyPredicate(PredicateType.SERVER.name());
        if (Objects.isNull(policyPredicate)) {
            policyPredicate = new SimplePolicyPredicate();
        }
        return policyPredicate;
    }

    protected List<Policy> getPolicies(Set<String> policyIds) {
        return policyDecisionManager.getPolicies(policyIds);
    }
}
