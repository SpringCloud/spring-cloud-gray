package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.event.server.TriggerType;
import cn.springcloud.gray.server.module.gray.GrayPolicyModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import cn.springcloud.gray.server.module.gray.domain.query.GrayDecisionQuery;
import cn.springcloud.gray.server.module.gray.domain.query.GrayPolicyQuery;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 08:55
 */
public class JPAGrayPolicyModule implements GrayPolicyModule {

    private GrayPolicyService grayPolicyService;
    private GrayDecisionService grayDecisionService;
    private GrayEventTrigger grayEventTrigger;


    public JPAGrayPolicyModule(
            GrayPolicyService grayPolicyService,
            GrayDecisionService grayDecisionService,
            GrayEventTrigger grayEventTrigger) {
        this.grayPolicyService = grayPolicyService;
        this.grayDecisionService = grayDecisionService;
        this.grayEventTrigger = grayEventTrigger;
    }

    @Override
    public Page<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace, Pageable pageable) {
        return grayPolicyService.listGrayPoliciesByNamespace(namespace, pageable);
    }

    @Override
    public List<GrayPolicy> listAllEnabledGrayPolicies() {
        return grayPolicyService.listEnabledGrayPolicies();
    }

    @Override
    public List<GrayPolicy> listAllGrayPolicies(Collection<Long> policyIds) {
        return grayPolicyService.findAllModel(policyIds);
    }

    @Override
    public List<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace) {
        return grayPolicyService.listEnabledGrayPoliciesByNamespace(namespace);
    }

    @Transactional
    @Override
    public GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy) {
        setDefaultValue(grayPolicy);
        GrayPolicy newRecord = grayPolicyService.saveModel(grayPolicy);
        triggerGrayEvent(TriggerType.ADD, newRecord);
        return newRecord;
    }

    @Transactional
    @Override
    public void deleteGrayPolicy(Long policyId, String userId) {
        GrayPolicy policy = grayPolicyService.findOneModel(policyId);
        if (policy != null && !policy.getDelFlag()) {
            policy.setDelFlag(true);
            policy.setOperator(userId);
            policy.setOperateTime(new Date());
            grayPolicyService.saveModel(policy);
            triggerGrayEvent(TriggerType.DELETE, policy);
        }
    }

    @Transactional
    @Override
    public void recoverGrayPolicy(Long policyId, String userId) {
        GrayPolicy policy = grayPolicyService.findOneModel(policyId);
        if (policy != null && policy.getDelFlag()) {
            policy.setDelFlag(false);
            policy.setOperator(userId);
            policy.setOperateTime(new Date());
            grayPolicyService.saveModel(policy);
            triggerGrayEvent(TriggerType.MODIFY, policy);
        }
    }

    @Override
    public GrayPolicy getGrayPolicy(Long policyId) {
        return grayPolicyService.findOneModel(policyId);
    }

    @Transactional
    @Override
    public GrayDecision saveGrayDecision(GrayDecision grayDecision) {
        setDefaultValue(grayDecision);
        GrayDecision newRecord = grayDecisionService.saveModel(grayDecision);
        triggerGrayEvent(TriggerType.ADD, newRecord);
        return newRecord;
    }

    @Transactional
    @Override
    public void deleteGrayDecision(Long decisionId, String userId) {
        GrayDecision decision = grayDecisionService.findOneModel(decisionId);
        if (decision != null && !decision.getDelFlag()) {
            decision.setDelFlag(true);
            decision.setOperator(userId);
            decision.setOperateTime(new Date());
            grayDecisionService.saveModel(decision);
            triggerGrayEvent(TriggerType.DELETE, decision);
        }
    }

    @Transactional
    @Override
    public void recoverGrayDecision(Long decisionId, String userId) {
        GrayDecision decision = grayDecisionService.findOneModel(decisionId);
        if (decision != null && decision.getDelFlag()) {
            decision.setDelFlag(false);
            decision.setOperator(userId);
            decision.setOperateTime(new Date());
            grayDecisionService.saveModel(decision);
            triggerGrayEvent(TriggerType.MODIFY, decision);
        }
    }

    @Override
    public GrayDecision getGrayDecision(Long id) {
        return grayDecisionService.findOneModel(id);
    }

    @Override
    public List<GrayDecision> listEnabledGrayDecisionsByPolicyId(Long policyId) {
        return grayDecisionService.findAllEnabledByPolicyId(policyId);
    }

    @Transactional
    @Override
    public GrayPolicyDecision newGrayPolicy(GrayPolicyDecision policyDecision) {
        setDefaultValue(policyDecision.getGrayPolicy());
        GrayPolicy newRecord = grayPolicyService.saveModel(policyDecision.getGrayPolicy());
        for (GrayDecision grayDecision : policyDecision.getGrayDecisions()) {
            grayDecision.setPolicyId(newRecord.getId());
            setDefaultValue(grayDecision);
        }
        List<GrayDecision> grayDecisions = grayDecisionService.saveModels(policyDecision.getGrayDecisions());

        triggerGrayEvent(TriggerType.ADD, newRecord);
        return new GrayPolicyDecision(newRecord, grayDecisions);
    }

    @Override
    public List<GrayPolicy> findAllGrayPolicies(Iterable<Long> policyIds, Boolean delFlag) {
        if (Objects.isNull(delFlag)) {
            return grayPolicyService.findAllModel(policyIds);
        }
        return grayPolicyService.findAllModel(policyIds, delFlag);
    }

    @Override
    public Page<GrayPolicy> queryGrayPolicies(GrayPolicyQuery query, Pageable pageable) {
        return grayPolicyService.queryGrayPolicies(query, pageable);
    }

    @Override
    public Page<GrayDecision> queryGrayDecisions(GrayDecisionQuery query, Pageable pageable) {
        return grayDecisionService.queryGrayDecisions(query, pageable);
    }

    protected void triggerGrayEvent(TriggerType triggerType, Object source) {
        grayEventTrigger.triggering(source, triggerType);
    }

    private void setDefaultValue(GrayDecision decision) {
        if (Objects.isNull(decision.getDelFlag())) {
            decision.setDelFlag(false);
        }
        if (Objects.isNull(decision.getOperateTime())) {
            decision.setOperateTime(new Date());
        }
    }

    private void setDefaultValue(GrayPolicy policy) {
        if (Objects.isNull(policy.getDelFlag())) {
            policy.setDelFlag(false);
        }
        if (Objects.isNull(policy.getOperateTime())) {
            policy.setOperateTime(new Date());
        }
    }
}
