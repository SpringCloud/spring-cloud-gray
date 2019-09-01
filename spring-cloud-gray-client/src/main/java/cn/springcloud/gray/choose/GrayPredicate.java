package cn.springcloud.gray.choose;

import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.servernode.ServerSpec;

public interface GrayPredicate {

    boolean apply(ServerSpec serverSpec);

    boolean apply(GrayDecisionInputArgs decisionInputArgs);

}
