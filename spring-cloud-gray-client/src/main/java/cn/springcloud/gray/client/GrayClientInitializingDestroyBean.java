package cn.springcloud.gray.client;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.InstanceLocalInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;


@Slf4j
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
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(clientConfig.grayEnrollDealyTimeInMs());
                    } catch (InterruptedException e) {
                    }
                    log.info("灰度注册自身实例...");
                    grayRegister();
                }, "GrayEnroll");
                t.start();
            } else {
                grayRegister();
            }
        }
    }

    public void shutdown() {
        if (instanceLocalInfo.isGray()) {
            grayManager.getGrayInformationClient().serviceDownline(
                    instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
        }
    }

    private void grayRegister() {
        grayManager.getGrayInformationClient().addGrayInstance(
                instanceLocalInfo.getServiceId(), instanceLocalInfo.getInstanceId());
        instanceLocalInfo.setGray(true);
    }
}
