package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;

import java.util.List;

public abstract class AbstractCommunicableGrayManager extends SimpleGrayManager implements CommunicableGrayManager {

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;

    public AbstractCommunicableGrayManager(
            GrayClientConfig grayClientConfig, GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            List<RequestInterceptor> requestInterceptors, InformationClient informationClient) {
        super(grayDecisionFactoryKeeper, requestInterceptors);
        this.grayClientConfig = grayClientConfig;
        this.informationClient = informationClient;
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


    }


}
