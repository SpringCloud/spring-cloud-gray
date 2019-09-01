package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerSpec;

import java.util.List;

public class DefaultGrayPredicate implements GrayPredicate {

    private RequestLocalStorage requestLocalStorage;
    private GrayManager grayManager;


    public DefaultGrayPredicate(RequestLocalStorage requestLocalStorage, GrayManager grayManager) {
        this.requestLocalStorage = requestLocalStorage;
        this.grayManager = grayManager;
    }

    @Override
    public boolean apply(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = GrayDecisionInputArgs
                .builder().grayRequest(requestLocalStorage.getGrayRequest()).server(serverSpec).build();
        return apply(decisionInputArgs);
    }

    @Override
    public boolean apply(GrayDecisionInputArgs decisionInputArgs) {
        GrayRequest grayRequest = decisionInputArgs.getGrayRequest();
        if(grayRequest==null){
            return false;
        }
        ServerSpec serverSpec = decisionInputArgs.getServer();
        if(serverSpec==null){
            return false;
        }

        List<GrayDecision> grayDecisions =
                grayManager.getGrayDecision(serverSpec.getServiceId(), serverSpec.getInstanceId());

        for (GrayDecision grayDecision : grayDecisions) {
            if (grayDecision.test(decisionInputArgs)) {
                return true;
            }
        }
        return false;
    }
}
