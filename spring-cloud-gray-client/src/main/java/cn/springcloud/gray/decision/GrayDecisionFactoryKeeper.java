package cn.springcloud.gray.decision;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.model.DecisionDefinition;

public interface GrayDecisionFactoryKeeper {

    GrayDecisionFactory getDecisionFactory(String name);


    GrayDecision getGrayDecision(DecisionDefinition decisionDefinition);
}
