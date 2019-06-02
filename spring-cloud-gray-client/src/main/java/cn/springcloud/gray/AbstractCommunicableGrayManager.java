package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;

public abstract class AbstractCommunicableGrayManager extends SimpleGrayManager implements CommunicableGrayManager {

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;

    public AbstractCommunicableGrayManager(
            GrayClientConfig grayClientConfig,
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            InformationClient informationClient) {
        super(grayDecisionFactoryKeeper);
        this.grayClientConfig = grayClientConfig;
        this.informationClient = informationClient;
    }

    public GrayClientConfig getGrayClientConfig() {
        return grayClientConfig;
    }

    @Override
    public InformationClient getGrayInformationClient() {
        return informationClient;
    }


}
