package cn.springcloud.gray.client.hooks;

import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.hook.StartShutdownHook;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-09-23 23:01
 */
@Slf4j
public class GrayClientEnrollHook implements StartShutdownHook {

    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;
    private InformationClient informationClient;
    private GrayClientConfig clientConfig;


    public GrayClientEnrollHook(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            InformationClient informationClient,
            GrayClientConfig clientConfig) {
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
        this.informationClient = informationClient;
        this.clientConfig = clientConfig;
    }

    @Override
    public void start() {
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

    @Override
    public void shutdown() {
//        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
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
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        GrayInstance grayInstance = new GrayInstance();
        grayInstance.setHost(instanceLocalInfo.getHost());
        grayInstance.setGrayStatus(GrayStatus.OPEN);
        grayInstance.setInstanceId(instanceLocalInfo.getInstanceId());
        grayInstance.setServiceId(instanceLocalInfo.getServiceId());
        grayInstance.setPort(instanceLocalInfo.getPort());

        informationClient.addGrayInstance(grayInstance);
        instanceLocalInfo.setGray(true);
    }

}
