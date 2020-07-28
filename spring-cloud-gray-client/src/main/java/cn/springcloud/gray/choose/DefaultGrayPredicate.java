package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.decision.BringPolicyDefinitionGrayDecision;
import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class DefaultGrayPredicate implements GrayPredicate {

    private RequestLocalStorage requestLocalStorage;
    private GrayManager grayManager;


    public DefaultGrayPredicate(RequestLocalStorage requestLocalStorage, GrayManager grayManager) {
        this.requestLocalStorage = requestLocalStorage;
        this.grayManager = grayManager;
    }

    @Override
    public boolean apply(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = GrayDecisionInputArgs.builder()
                .grayRequest(requestLocalStorage.getGrayRequest())
                .server(serverSpec)
                .build();
        return apply(decisionInputArgs);
    }

    @Override
    public boolean apply(GrayDecisionInputArgs decisionInputArgs) {
        GrayRequest grayRequest = decisionInputArgs.getGrayRequest();
        if (grayRequest == null) {
            return false;
        }
        ServerSpec serverSpec = decisionInputArgs.getServer();
        if (serverSpec == null) {
            return false;
        }

        List<GrayDecision> grayDecisions =
                grayManager.getGrayDecision(serverSpec.getServiceId(), serverSpec.getInstanceId());

        for (int i = 0; i < grayDecisions.size(); i++) {
            GrayDecision grayDecision = grayDecisions.get(i);
            boolean testValue = grayDecision.test(decisionInputArgs);
            printGrayPolicyDecisionInfo(decisionInputArgs, grayDecision, testValue, grayDecisions.size(), i);
            if (testValue) {
                return true;
            }
        }
        return false;
    }


    private void printGrayPolicyDecisionInfo(
            GrayDecisionInputArgs decisionInputArgs,
            GrayDecision grayDecision,
            boolean testValue,
            int decisionLength,
            int currentDecisionIndex) {
        if (!log.isDebugEnabled()) {
            return;
        }
        try {
            if (grayDecision instanceof BringPolicyDefinitionGrayDecision) {
                BringPolicyDefinitionGrayDecision policyDefinitionGrayDecision = (BringPolicyDefinitionGrayDecision) grayDecision;
                PolicyDefinition policyDefinition = policyDefinitionGrayDecision.getPolicyDefinition();
                log.debug("【灰度决策】服务'{}'的实例'{}' ,'{}-{}'决策结果为'{}'; \ndecisionInputArgs: {}, \npolicyDefinition: {}",
                        decisionInputArgs.getGrayRequest().getServiceId(),
                        decisionInputArgs.getServer().getInstanceId(),
                        decisionLength,
                        currentDecisionIndex + 1,
                        testValue,
                        JsonUtils.toJson(decisionInputArgs),
                        JsonUtils.toJson(policyDefinition));
            }
            log.debug("【灰度决策】服务'{}'的实例'{}' ,'{}-{}'决策结果为'{}'; \ndecisionInputArgs: {}",
                    decisionInputArgs.getGrayRequest().getServiceId(),
                    decisionInputArgs.getServer().getInstanceId(),
                    decisionLength,
                    currentDecisionIndex + 1,
                    testValue,
                    JsonUtils.toJson(decisionInputArgs));
        } catch (IOException e) {
            log.error("", e);
        }

    }
}
