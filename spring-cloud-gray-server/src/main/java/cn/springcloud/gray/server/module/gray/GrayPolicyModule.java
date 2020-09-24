package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import cn.springcloud.gray.server.module.gray.domain.query.GrayDecisionQuery;
import cn.springcloud.gray.server.module.gray.domain.query.GrayPolicyQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author saleson
 * @date 2020-03-17 00:05
 */
public interface GrayPolicyModule {

    Page<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace, Pageable pageable);

    List<GrayPolicy> listAllEnabledGrayPolicies();

    List<GrayPolicy> listAllGrayPolicies(Collection<Long> policyIds);

    List<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace);

    GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy);

    void deleteGrayPolicy(Long policyId, String userId);

    void recoverGrayPolicy(Long policyId, String userId);

    GrayPolicy getGrayPolicy(Long policyId);

    GrayDecision saveGrayDecision(GrayDecision grayDecision);

    void deleteGrayDecision(Long decisionId, String userId);

    void recoverGrayDecision(Long decisionId, String userId);

    GrayDecision getGrayDecision(Long id);

    List<GrayDecision> listEnabledGrayDecisionsByPolicyId(Long policyId);

    GrayPolicyDecision newGrayPolicy(GrayPolicyDecision policyDecision);

    List<GrayPolicy> findAllGrayPolicies(Iterable<Long> policyIds, Boolean delFlag);

    Page<GrayPolicy> queryGrayPolicies(GrayPolicyQuery query, Pageable pageable);

    Page<GrayDecision> queryGrayDecisions(GrayDecisionQuery query, Pageable pageable);
}
