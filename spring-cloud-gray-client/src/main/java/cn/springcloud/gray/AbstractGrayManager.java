package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.MultiGrayDecision;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.PolicyDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;

import java.util.*;


/**
 * GrayManager的抽象实现类实现了基础的获取灰度列表， 创建灰度决策对象的能力
 */
public abstract class AbstractGrayManager implements UpdateableGrayManager {
    private static final Logger log = LoggerFactory.getLogger(AbstractGrayManager.class);


    private GrayDecisionFactoryKeeper grayDecisionFactoryKeeper;
    private Map<String, List<RequestInterceptor>> requestInterceptors = new HashMap<>();
    private List<RequestInterceptor> communalRequestInterceptors = ListUtils.EMPTY_LIST;


    public AbstractGrayManager(
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper) {
        this.grayDecisionFactoryKeeper = grayDecisionFactoryKeeper;
    }


    @Override
    public List<RequestInterceptor> getRequeestInterceptors(String interceptroType) {
        List<RequestInterceptor> list = requestInterceptors.get(interceptroType);
        if (list == null) {
            return communalRequestInterceptors;
        }
        return list;
    }


    @Override
    public List<GrayDecision> getGrayDecision(GrayInstance instance) {
        List<PolicyDefinition> policyDefinitions = instance.getPolicyDefinitions();
        List<GrayDecision> grayDecisions = new ArrayList<>(policyDefinitions.size());

        for (PolicyDefinition policyDefinition : policyDefinitions) {
            if (CollectionUtils.isEmpty(policyDefinition.getList())) {
                continue;
            }
            GrayDecision decision = createGrayDecision(policyDefinition);
            if (decision != null) {
                grayDecisions.add(decision);
            }
        }

        return grayDecisions;
    }

    @Override
    public List<GrayDecision> getGrayDecision(String serviceId, String instanceId) {
        return getGrayDecision(getGrayInstance(serviceId, instanceId));
    }


    private GrayDecision createGrayDecision(PolicyDefinition policyDefinition) {
        try {
            MultiGrayDecision decision = new MultiGrayDecision(GrayDecision.allow());
            for (DecisionDefinition decisionDefinition : policyDefinition.getList()) {
                decision = decision.and(grayDecisionFactoryKeeper.getGrayDecision(decisionDefinition));
            }
            return decision;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }



    public void setRequestInterceptors(Collection<RequestInterceptor> requestInterceptors) {
        Map<String, List<RequestInterceptor>> requestInterceptorMap = new HashMap<>();
        List<RequestInterceptor> all = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(requestInterceptors)) {
            for (RequestInterceptor interceptor : requestInterceptors) {
                if (StringUtils.equals(interceptor.interceptroType(), "all")) {
                    all.add(interceptor);
                } else {
                    List<RequestInterceptor> interceptors = requestInterceptorMap.get(interceptor.interceptroType());
                    if (interceptors == null) {
                        interceptors = new ArrayList<>();
                        requestInterceptorMap.put(interceptor.interceptroType(), interceptors);
                    }
                    interceptors.add(interceptor);
                }
            }
            putTypeAllTo(requestInterceptorMap, all);
            this.communalRequestInterceptors = all;
        }
        this.communalRequestInterceptors = all;
        this.requestInterceptors = requestInterceptorMap;

    }

    private void putTypeAllTo(Map<String, List<RequestInterceptor>> requestInterceptorMap, List<RequestInterceptor> all) {
        if (all.isEmpty()) {
            return;
        }
        requestInterceptorMap.values().forEach(list -> {
            list.addAll(all);
            OrderComparator.sort(list);
        });
    }

}
