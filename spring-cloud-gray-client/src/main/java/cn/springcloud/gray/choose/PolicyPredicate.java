package cn.springcloud.gray.choose;

import cn.springcloud.gray.decision.DecisionInputArgs;

/**
 * @author saleson
 * @date 2020-04-08 00:19
 */
public interface PolicyPredicate {


    String predicateType();

    boolean test(DecisionInputArgs decisionInputArgs);
}
