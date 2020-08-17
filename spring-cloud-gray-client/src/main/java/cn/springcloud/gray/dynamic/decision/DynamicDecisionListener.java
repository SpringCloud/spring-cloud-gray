package cn.springcloud.gray.dynamic.decision;

import cn.springcloud.gray.decision.DefaultCachePolicyDecisionManager;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.dynamiclogic.DynamicLogicEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author saleson
 * @date 2019-12-28 10:04
 */
public class DynamicDecisionListener implements ApplicationListener<DynamicLogicEvent> {

    private PolicyDecisionManager policyDecisionManager;

    public DynamicDecisionListener(PolicyDecisionManager policyDecisionManager) {
        this.policyDecisionManager = policyDecisionManager;
    }

    @Override
    public void onApplicationEvent(DynamicLogicEvent event) {
        if (policyDecisionManager instanceof DefaultCachePolicyDecisionManager) {
            DefaultCachePolicyDecisionManager cachePolicyDecisionManager = (DefaultCachePolicyDecisionManager) policyDecisionManager;
            cachePolicyDecisionManager.invalidateAllCache();
        }
    }
}
