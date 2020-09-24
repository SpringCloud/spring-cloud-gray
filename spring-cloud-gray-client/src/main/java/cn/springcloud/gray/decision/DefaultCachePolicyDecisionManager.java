package cn.springcloud.gray.decision;

import cn.springcloud.gray.Cache;
import cn.springcloud.gray.choose.PolicyPredicate;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.PolicyDefinition;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author saleson
 * @date 2020-04-08 23:03
 */
public class DefaultCachePolicyDecisionManager implements PolicyDecisionManager {

    private Cache<String, Policy> policyCache;
    private PolicyDecisionManager delegater;

    public DefaultCachePolicyDecisionManager(Cache<String, Policy> policyCache, PolicyDecisionManager delegater) {
        this.policyCache = policyCache;
        this.delegater = delegater;
    }

    @Override
    public Policy getPolicy(String policyId) {
        return getCachePolicy(policyId, () -> delegater.getPolicy(policyId));
    }


    @Override
    public PolicyInfo removePolicy(String policyId) {
        invalidateCache(policyId);
        return delegater.removePolicy(policyId);
    }

    @Override
    public PolicyInfo getPolicyInfo(String policyId) {
        return delegater.getPolicyInfo(policyId);
    }

    @Override
    public void clearAllPolicy() {
        delegater.clearAllPolicy();
        invalidateAllCache();
    }


    @Override
    public void setPolicyInfo(PolicyInfo policyInfo) {
        delegater.setPolicyInfo(policyInfo);
        invalidateCache(policyInfo.getPolicyId());
    }


    @Override
    public void setPolicyDefinition(PolicyDefinition policyDefinition) {
        delegater.setPolicyDefinition(policyDefinition);
        invalidateCache(policyDefinition.getPolicyId());
    }


    @Override
    public void setDecisionDefinition(String policyId, DecisionDefinition decisionDefinition) {
        delegater.setDecisionDefinition(policyId, decisionDefinition);
        invalidateCache(policyId);
    }


    @Override
    public void removeDecisionDefinition(String policyId, String decisionId) {
        delegater.removeDecisionDefinition(policyId, decisionId);
        invalidateCache(policyId);
    }

    @Override
    public void registerPolicyPredicate(PolicyPredicate policyPredicate) {
        delegater.registerPolicyPredicate(policyPredicate);
    }

    @Override
    public PolicyPredicate removePolicyPredicate(String predicateType) {
        return delegater.removePolicyPredicate(predicateType);
    }

    @Override
    public PolicyPredicate getPolicyPredicate(String predicateType) {
        return delegater.getPolicyPredicate(predicateType);
    }

    @Override
    public boolean testPolicyPredicate(String predicateType, DecisionInputArgs decisionInputArgs, boolean defaultResult) {
        return delegater.testPolicyPredicate(predicateType, decisionInputArgs, defaultResult);
    }

    @Override
    public Map<String, PolicyInfo> getAllPolicyInfos() {
        return delegater.getAllPolicyInfos();
    }


    protected Policy getCachePolicy(String key, Supplier<Policy> supplier) {
        return policyCache.get(key, k -> supplier.get());
    }


    public void invalidateCache(String key) {
        policyCache.invalidate(key);
    }


    public void invalidateAllCache() {
        policyCache.invalidateAll();
    }

}
