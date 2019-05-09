package cn.springcloud.gray;


import cn.springcloud.gray.decision.factory.GrayDecisionFactory;

import java.util.List;

public class DefaultGrayManager extends CommunicableGrayManager {


    public DefaultGrayManager(GrayClientConfig grayClientConfig, List<GrayDecisionFactory> decisionFactories, List<RequestInterceptor> requestInterceptors) {
        super(grayClientConfig, decisionFactories, requestInterceptors);
    }


}
