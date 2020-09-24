package cn.springcloud.gray.decision;

import cn.springcloud.gray.choose.PolicyPredicate;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.PolicyDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-04-07 22:54
 */
public class DefaultPolicyDecisionManager implements PolicyDecisionManager {

    private Map<String, PolicyInfo> policyInfos = new ConcurrentHashMap<>();
    private Map<String, PolicyPredicate> policyPredicates = new ConcurrentHashMap<>();

    private GrayDecisionFactoryKeeper decisionFactoryKeeper;

    public DefaultPolicyDecisionManager(GrayDecisionFactoryKeeper decisionFactoryKeeper) {
        this.decisionFactoryKeeper = decisionFactoryKeeper;
    }

    @Override
    public Policy getPolicy(String policyId) {
        PolicyInfo policyInfo = getPolicyInfo(policyId);
        if (Objects.isNull(policyInfo)) {
            return null;
        }
        Policy policy = new Policy();
        policy.setId(policyId);
        List<GrayDecision> decisions = new ArrayList<>(policyInfo.getDecisionDefinitions().size());
        for (DecisionDefinition decisionDefinition : policyInfo.getDecisionDefinitions().values()) {
            decisions.add(decisionFactoryKeeper.getGrayDecision(decisionDefinition));
        }
        policy.setDecisions(decisions);
        return policy;
    }

    @Override
    public PolicyInfo removePolicy(String policyId) {
        return policyInfos.remove(policyId);
    }

    @Override
    public PolicyInfo getPolicyInfo(String policyId) {
        return policyInfos.get(policyId);
    }

    @Override
    public void clearAllPolicy() {
        policyInfos.clear();
    }

    @Override
    public void setPolicyInfo(PolicyInfo policyInfo) {
        policyInfos.put(policyInfo.getPolicyId(), policyInfo);
    }

    @Override
    public void setPolicyDefinition(PolicyDefinition policyDefinition) {
        PolicyInfo policyInfo = new PolicyInfo();
        policyInfo.setAlias(policyDefinition.getAlias())
                .setPolicyId(policyDefinition.getPolicyId());
        policyDefinition.getList()
                .forEach(policyInfo::setDecisionDefinition);
        setPolicyInfo(policyInfo);
    }

    @Override
    public void setDecisionDefinition(String policyId, DecisionDefinition decisionDefinition) {
        PolicyInfo policyInfo = getExistingPolicyInfo(policyId);
        policyInfo.setDecisionDefinition(decisionDefinition);
    }

    @Override
    public void removeDecisionDefinition(String policyId, String decisionId) {
        PolicyInfo policyInfo = getExistingPolicyInfo(policyId);
        policyInfo.removeDecisionDefinition(decisionId);
    }

    @Override
    public void registerPolicyPredicate(PolicyPredicate policyPredicate) {
        policyPredicates.put(policyPredicate.predicateType().toLowerCase(), policyPredicate);
    }

    @Override
    public PolicyPredicate removePolicyPredicate(String predicateType) {
        return policyPredicates.remove(predicateType);
    }

    @Override
    public PolicyPredicate getPolicyPredicate(String predicateType) {
        return policyPredicates.get(predicateType.toLowerCase());
    }

    @Override
    public boolean testPolicyPredicate(String predicateType, DecisionInputArgs decisionInputArgs, boolean defaultResult) {
        PolicyPredicate policyPredicate = getPolicyPredicate(predicateType);
        if (Objects.isNull(policyPredicate)) {
            return defaultResult;
        }
        return policyPredicate.test(decisionInputArgs);
    }

    @Override
    public Map<String, PolicyInfo> getAllPolicyInfos() {
        return Collections.unmodifiableMap(policyInfos);
    }

    private PolicyInfo getExistingPolicyInfo(String policyId) {
        PolicyInfo policyInfo = getPolicyInfo(policyId);
        assertPolicyInfo(policyInfo);
        return policyInfo;
    }

    private void assertPolicyInfo(PolicyInfo policyInfo) {
        if (Objects.isNull(policyInfo)) {
            throw new NullPointerException("PolicyInfo is null");
        }
    }
}
