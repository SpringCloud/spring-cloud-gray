package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-17 00:05
 */
public interface GrayPolicyModule {

    Page<GrayPolicy> listGrayPoliciesByNamespace(String namespace, Pageable pageable);

    List<GrayPolicy> listGrayPoliciesByNamespace(String namespace);

    Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable);

    GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy);

    void deleteGrayPolicy(Long policyId, String userId);

    void recoverGrayPolicy(Long policyId, String userId);

    GrayDecision saveGrayDecision(GrayDecision grayDecision);

    void deleteGrayDecision(Long decisionId, String userId);

    void recoverGrayDecision(Long decisionId, String userId);

    GrayDecision getGrayDecision(Long id);

    List<GrayDecision> listGrayDecisionsByPolicyId(Long policyId);

    GrayPolicyDecision newGrayPolicy(GrayPolicyDecision policyDecision);

    List<GrayPolicy> findAllGrayPolicies(Iterable<Long> policyIds, Boolean delFlag);
}
