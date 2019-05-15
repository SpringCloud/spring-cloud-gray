package cn.springcloud.gray;

import cn.springcloud.gray.communication.HttpInformationClient;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.communication.RetryableInformationClient;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;

import java.util.List;

public abstract class AbstractCommunicableGrayManager extends SimpleGrayManager implements CommunicableGrayManager {

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;

    public AbstractCommunicableGrayManager(GrayClientConfig grayClientConfig, GrayDecisionFactoryKeeper grayDecisionFactoryKeeper, List<RequestInterceptor> requestInterceptors) {
        super(grayDecisionFactoryKeeper, requestInterceptors);
        this.grayClientConfig = grayClientConfig;
        createInformationClient();
    }

    public GrayClientConfig getGrayClientConfig() {
        return grayClientConfig;
    }

    @Override
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
