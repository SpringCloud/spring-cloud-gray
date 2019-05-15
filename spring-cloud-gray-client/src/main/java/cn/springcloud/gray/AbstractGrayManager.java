package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.MultiGrayDecision;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
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
public abstract class AbstractGrayManager implements GrayManager {
    private static final Logger log = LoggerFactory.getLogger(AbstractGrayManager.class);


    private GrayDecisionFactoryKeeper grayDecisionFactoryKeeper;
    private Map<String, List<RequestInterceptor>> requestInterceptors = new HashMap<>();


    public AbstractGrayManager(
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper, List<RequestInterceptor> requestInterceptors) {
        initRequestInterceptors(requestInterceptors);
        this.grayDecisionFactoryKeeper = grayDecisionFactoryKeeper;
    }


    @Override
    public List<RequestInterceptor> getRequeestInterceptors(String interceptroType) {
        List<RequestInterceptor> list = requestInterceptors.get(interceptroType);
        if (list == null) {
            return ListUtils.EMPTY_LIST;
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
        MultiGrayDecision decision = new MultiGrayDecision(GrayDecision.allow());
        for (DecisionDefinition decisionDefinition : policyDefinition.getList()) {
            decision = decision.and(grayDecisionFactoryKeeper.getGrayDecision(decisionDefinition));
        }
        return decision;
    }


    private void initRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
        if (requestInterceptors == null || requestInterceptors.isEmpty()) {
            return;
        }
        List<RequestInterceptor> all = new ArrayList<>();
        for (RequestInterceptor interceptor : requestInterceptors) {
            if (StringUtils.equals(interceptor.interceptroType(), "all")) {
                all.add(interceptor);
            } else {
                List<RequestInterceptor> interceptors = this.requestInterceptors.get(interceptor.interceptroType());
                if (interceptors == null) {
                    interceptors = new ArrayList<>();
                    this.requestInterceptors.put(interceptor.interceptroType(), interceptors);
                }
                interceptors.add(interceptor);
            }
        }
        putTypeAllTo(all);
    }

    private void putTypeAllTo(List<RequestInterceptor> all) {
        if (all.isEmpty()) {
            return;
        }
        requestInterceptors.values().forEach(list -> {
            list.addAll(all);
            OrderComparator.sort(list);
        });
    }

}
