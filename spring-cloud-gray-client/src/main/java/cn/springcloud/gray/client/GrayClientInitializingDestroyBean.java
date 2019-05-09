package cn.springcloud.gray.client;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.InstanceLocalInfo;
import org.springframework.beans.factory.InitializingBean;

import java.util.Timer;


public class GrayClientInitializingDestroyBean implements InitializingBean {

    private CommunicableGrayManager grayManager;
    private InstanceLocalInfo instanceLocalInfo;

    public GrayClientInitializingDestroyBean(CommunicableGrayManager grayManager, InstanceLocalInfo instanceLocalInfo) {
        this.grayManager = grayManager;
        this.instanceLocalInfo = instanceLocalInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GrayClientConfig clientConfig = grayManager.getGrayClientConfig();
        if (clientConfig.isGrayEnroll()) {
            if (clientConfig.grayEnrollDealyTimeInMs() > 0) {

                //todo
            } else {
                grayManager.getGrayInformationClient().addGrayInstance(
                        instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
                instanceLocalInfo.setGray(true);
            }
        }
    }

    public void shutdown() {
        if (instanceLocalInfo.isGray()) {
            grayManager.getGrayInformationClient().serviceDownline(
                    instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
        }
    }
}
