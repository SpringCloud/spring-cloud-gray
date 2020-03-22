package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.server.module.gray.GrayPolicyModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicyDecision;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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


    public JPAGrayPolicyModule(GrayPolicyService grayPolicyService, GrayDecisionService grayDecisionService) {
        this.grayPolicyService = grayPolicyService;
        this.grayDecisionService = grayDecisionService;
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
    public List<GrayPolicy> listEnabledGrayPoliciesByNamespace(String namespace) {
        return grayPolicyService.listEnabledGrayPoliciesByNamespace(namespace);
    }

    @Override
    public Page<GrayDecision> listGrayDecisionsByPolicyId(Long policyId, Pageable pageable) {
        return grayDecisionService.listGrayDecisionsByPolicyId(policyId, pageable);
    }

    @Transactional
    @Override
    public GrayPolicy saveGrayPolicy(GrayPolicy grayPolicy) {
        setDefaultValue(grayPolicy);
        GrayPolicy newRecord = grayPolicyService.saveModel(grayPolicy);
        //todo 加事件
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
            //todo 加事件
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
            //todo 加事件
        }
    }

    @Transactional
    @Override
    public GrayDecision saveGrayDecision(GrayDecision grayDecision) {
        setDefaultValue(grayDecision);
        GrayDecision newRecord = grayDecisionService.saveModel(grayDecision);
        //todo 加事件
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
            //todo 加事件
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
            //todo 加事件
        }
    }

    @Override
    public GrayDecision getGrayDecision(Long id) {
        return grayDecisionService.findOneModel(id);
    }

    @Override
    public List<GrayDecision> listGrayDecisionsByPolicyId(Long policyId) {
        return grayDecisionService.findByPolicyId(policyId);
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

        //todo 加事件
        return new GrayPolicyDecision(newRecord, grayDecisions);
    }

    @Override
    public List<GrayPolicy> findAllGrayPolicies(Iterable<Long> policyIds, Boolean delFlag) {
        if (Objects.isNull(delFlag)) {
            return grayPolicyService.findAllModel(policyIds);
        }
        return grayPolicyService.findAllModel(policyIds, delFlag);
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
