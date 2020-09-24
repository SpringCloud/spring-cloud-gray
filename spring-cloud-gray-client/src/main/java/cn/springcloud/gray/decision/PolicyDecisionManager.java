package cn.springcloud.gray.decision;

import cn.springcloud.gray.choose.PolicyPredicate;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.PolicyDefinition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-04-07 22:10
 */
public interface PolicyDecisionManager {


    Policy getPolicy(String policyId);

    default List<Policy> getPolicies(Collection<String> policyIds) {
        return policyIds.stream()
                .map(this::getPolicy)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    PolicyInfo removePolicy(String policyId);


    PolicyInfo getPolicyInfo(String policyId);

    void clearAllPolicy();


    void setPolicyInfo(PolicyInfo policyInfo);


    void setPolicyDefinition(PolicyDefinition policyDefinition);


    void setDecisionDefinition(String policyId, DecisionDefinition decisionDefinition);


    void removeDecisionDefinition(String policyId, String decisionId);


    void registerPolicyPredicate(PolicyPredicate policyPredicate);


    PolicyPredicate removePolicyPredicate(String predicateType);


    PolicyPredicate getPolicyPredicate(String predicateType);


    default boolean testPolicyPredicate(String predicateType, DecisionInputArgs decisionInputArgs) {
        return testPolicyPredicate(predicateType, decisionInputArgs, false);
    }

    boolean testPolicyPredicate(String predicateType, DecisionInputArgs decisionInputArgs, boolean defaultResult);

    Map<String, PolicyInfo> getAllPolicyInfos();
}
