package cn.springcloud.gray.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author saleson
 * @date 2020-05-09 23:37
 */
public class RoutePolicies {

    private Set<String> policies = new ConcurrentSkipListSet<>();


    public void addPolicyId(String policyId) {
        policies.add(policyId);
    }

    public void addPolicyIds(Collection<String> policyIds) {
        policies.addAll(policyIds);
    }


    public void removePolicyId(String policyId) {
        policies.remove(policyId);
    }

    public Set<String> getPolicyIds() {
        return Collections.unmodifiableSet(policies);
    }

    public boolean isEmpty() {
        return policies.isEmpty();
    }
}
