package cn.springcloud.gray.client;

import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoAware;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;


@Slf4j
public class GrayClientEnrollInitializingDestroyBean implements InitializingBean, InstanceLocalInfoAware {

    private InformationClient informationClient;
    private InstanceLocalInfo instanceLocalInfo;
    private GrayClientConfig clientConfig;

    public GrayClientEnrollInitializingDestroyBean(
            InformationClient informationClient, GrayClientConfig clientConfig) {
        this.informationClient = informationClient;
        this.clientConfig = clientConfig;
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
                    registerGrayInstance();
                }, "GrayEnroll");
                t.start();
            } else {
                registerGrayInstance();
            }
        }
    }

    public void shutdown() {
//        if (instanceLocalInfo.isGray()) {
//            informationClient.serviceDownline(
//                    instanceLocalInfo.getInstanceId());
//        }
    }

    private void registerGrayInstance() {
        log.info("灰度注册自身实例...");
        try {
            grayRegister();
            log.info("灰度注册成功.");
        } catch (Exception e) {
            log.error("灰度注册失败.", e);
            throw e;
        }
    }

    private void grayRegister() {
        GrayInstance grayInstance = new GrayInstance();
        grayInstance.setHost(instanceLocalInfo.getHost());
        grayInstance.setGrayStatus(GrayStatus.OPEN);
        grayInstance.setInstanceId(instanceLocalInfo.getInstanceId());
        grayInstance.setServiceId(instanceLocalInfo.getServiceId());
        grayInstance.setPort(instanceLocalInfo.getPort());

        informationClient.addGrayInstance(grayInstance);
        instanceLocalInfo.setGray(true);
    }

    @Override
    public void setInstanceLocalInfo(InstanceLocalInfo instanceLocalInfo) {
        this.instanceLocalInfo = instanceLocalInfo;
    }
}
