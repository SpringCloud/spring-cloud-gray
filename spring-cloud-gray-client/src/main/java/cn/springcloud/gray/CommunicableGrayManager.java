package cn.springcloud.gray;

import cn.springcloud.gray.communication.HttpInformationClient;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.communication.RetryableInformationClient;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;

import java.util.List;

public abstract class CommunicableGrayManager extends SimpleGrayManager {

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;

    public CommunicableGrayManager(GrayClientConfig grayClientConfig, List<GrayDecisionFactory> decisionFactories, List<RequestInterceptor> requestInterceptors) {
        super(decisionFactories, requestInterceptors);
        this.grayClientConfig = grayClientConfig;
        createInformationClient();
    }

    public GrayClientConfig getGrayClientConfig() {
        return grayClientConfig;
    }

    public InformationClient getGrayInformationClient() {
        return informationClient;
    }

    protected void createInformationClient() {

        GrayClientConfig clientConfig = getGrayClientConfig();
        InformationClient httpClient = new HttpInformationClient(clientConfig.getServerUrl());
        if (clientConfig.isRetryable()) {
            informationClient = new RetryableInformationClient(Math.max(3, clientConfig.getRetryNumberOfRetries()), httpClient);
        } else {
            informationClient = httpClient;
        }

    }


}
