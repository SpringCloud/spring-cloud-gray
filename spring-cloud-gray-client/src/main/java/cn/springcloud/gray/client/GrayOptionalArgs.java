package cn.springcloud.gray.client;

import cn.springcloud.gray.core.GrayDecisionFactory;
import cn.springcloud.gray.core.InformationClient;

public class GrayOptionalArgs {

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;
    private GrayDecisionFactory decisionFactory;


    public GrayClientConfig getGrayClientConfig() {
        return grayClientConfig;
    }


    public GrayDecisionFactory getDecisionFactory() {
        return decisionFactory;
    }


    public void setGrayClientConfig(GrayClientConfig grayClientConfig) {
        this.grayClientConfig = grayClientConfig;
    }


    public void setDecisionFactory(GrayDecisionFactory decisionFactory) {
        this.decisionFactory = decisionFactory;
    }


    public InformationClient getInformationClient() {
        return informationClient;
    }

    public void setInformationClient(InformationClient informationClient) {
        this.informationClient = informationClient;
    }
}


