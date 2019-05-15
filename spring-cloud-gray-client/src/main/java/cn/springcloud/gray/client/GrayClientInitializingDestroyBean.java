package cn.springcloud.gray.client;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;


@Slf4j
public class GrayClientInitializingDestroyBean implements InitializingBean {

    private CommunicableGrayManager grayManager;
    private InstanceLocalInfo instanceLocalInfo;
    private GrayClientConfig clientConfig;

    public GrayClientInitializingDestroyBean(
            CommunicableGrayManager grayManager, GrayClientConfig clientConfig, InstanceLocalInfo instanceLocalInfo) {
        this.grayManager = grayManager;
        this.clientConfig = clientConfig;
        this.instanceLocalInfo = instanceLocalInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
                    instanceLocalInfo.getInstanceId());
        }
    }

    private void grayRegister() {
        GrayInstance grayInstance = new GrayInstance();
        grayInstance.setHost(instanceLocalInfo.getHost());
        grayInstance.setGrayStatus(GrayStatus.OPEN);
        grayInstance.setInstanceId(instanceLocalInfo.getInstanceId());
        grayInstance.setServiceId(instanceLocalInfo.getServiceId());
        grayInstance.setPort(instanceLocalInfo.getPort());

        grayManager.getGrayInformationClient().addGrayInstance(grayInstance);
        instanceLocalInfo.setGray(true);
    }
}
