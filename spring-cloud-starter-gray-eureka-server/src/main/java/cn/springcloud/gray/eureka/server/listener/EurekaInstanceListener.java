package cn.springcloud.gray.eureka.server.listener;


import cn.springcloud.gray.eureka.server.EurekaInstatnceTransformer;
import cn.springcloud.gray.eureka.server.communicate.GrayCommunicateClient;
import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import com.netflix.eureka.registry.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;


@Slf4j
public class EurekaInstanceListener {

    private GrayCommunicateClient communicateClient;

    public EurekaInstanceListener(GrayCommunicateClient communicateClient) {
        this.communicateClient = communicateClient;
    }


    /**
     * 监听eureka实例下线事件，并发送信息给灰度服务器
     *
     * @param event eureka 实例下线事件
     */
    @EventListener
    public void listenDown(EurekaInstanceCanceledEvent event) {
        InstanceRegistry registry = (InstanceRegistry) event.getSource();
        com.netflix.appinfo.InstanceInfo instanceInfo =
                registry.getApplication(event.getAppName()).getByInstanceId(event.getServerId());
        sendNotice(instanceInfo, InstanceStatus.DOWN, "DOWN");
    }


    /**
     * 监听eureka实例心跳事件，并发送信息给灰度服务器
     *
     * @param event eureka 实例心跳事件
     */
    @EventListener
    public void listenRenew(EurekaInstanceRenewedEvent event) {
        com.netflix.appinfo.InstanceInfo instanceInfo = event.getInstanceInfo();
        sendNotice(instanceInfo, InstanceStatus.UP, "REGISTERED");
    }


    /**
     * 监听eureka实例注册事件，并发送信息给灰度服务器
     *
     * @param event eureka 实例注册事件
     */
    @EventListener
    public void listenRegistered(EurekaInstanceRegisteredEvent event) {
        com.netflix.appinfo.InstanceInfo instanceInfo = event.getInstanceInfo();
        InstanceStatus instanceStatus = EurekaInstatnceTransformer.toGrayInstanceStatus(instanceInfo.getStatus());
        sendNotice(instanceInfo, instanceStatus, "REGISTERED");
    }


    /**
     * 发送实例信息给灰度服务器
     *
     * @param instanceInfo   实例信息
     * @param instanceStatus 事件状态
     * @param eventType      eureka事件类型
     */
    private void sendNotice(com.netflix.appinfo.InstanceInfo instanceInfo, InstanceStatus instanceStatus, String eventType) {
        log.info(MarkerFactory.getMarker(eventType), "{}  serviceId：{}, instanceId：{} ",
                eventType, instanceInfo.getAppName(), instanceInfo.getInstanceId());

        sendNotice(InstanceInfo.builder()
                .instanceId(instanceInfo.getInstanceId())
                .serviceId(instanceInfo.getVIPAddress())
                .instanceStatus(instanceStatus)
                .build(), eventType);
    }


    /**
     * 发送实例信息给灰度服务器
     *
     * @param instanceInfo 实例信息
     * @param eventType    事件状态
     */
    private void sendNotice(InstanceInfo instanceInfo, String eventType) {
        try {
            communicateClient.noticeInstanceInfo(instanceInfo);
        } catch (Exception e) {
            log.error("发送实例{}消息失败,serviceId:{}, instanceId:{}",
                    eventType, instanceInfo.getServiceId(), instanceInfo.getInstanceId(), e);
        }
    }


}
