package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.utils.NameUtils;

/**
 * 灰度决策的工厂类
 */
public interface GrayDecisionFactory {

    default String name() {
        return NameUtils.normalizeFilterFactoryName(getClass());
    }


    GrayDecision getDecision(DecisionDefinition decisionDefinition);
}
