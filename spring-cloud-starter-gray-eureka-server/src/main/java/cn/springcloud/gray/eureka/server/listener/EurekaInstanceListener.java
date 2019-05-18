package cn.springcloud.gray.eureka.server.listener;


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

    @EventListener
    public void listenDown(EurekaInstanceCanceledEvent event) {
        InstanceRegistry registry = (InstanceRegistry) event.getSource();
        com.netflix.appinfo.InstanceInfo instanceInfo =
                registry.getApplication(event.getAppName()).getByInstanceId(event.getServerId());
        sendNotice(instanceInfo, InstanceStatus.DOWN, "DOWN");
    }


    @EventListener
    public void listenRenew(EurekaInstanceRenewedEvent event) {
        com.netflix.appinfo.InstanceInfo instanceInfo = event.getInstanceInfo();
        sendNotice(instanceInfo, InstanceStatus.UP, "REGISTERED");
    }


    @EventListener
    public void listenRegistered(EurekaInstanceRegisteredEvent event) {
        com.netflix.appinfo.InstanceInfo instanceInfo = event.getInstanceInfo();
        InstanceStatus instanceStatus = InstanceStatus.DOWN;
        if (instanceInfo.getStatus() == com.netflix.appinfo.InstanceInfo.InstanceStatus.UP) {
            instanceStatus = InstanceStatus.UP;
        }
        sendNotice(instanceInfo, instanceStatus, "REGISTERED");
    }

    private void sendNotice(com.netflix.appinfo.InstanceInfo instanceInfo, InstanceStatus instanceStatus, String eventType) {
        log.info(MarkerFactory.getMarker(eventType), "{}  serviceId：{}, instanceId：{} ",
                eventType, instanceInfo.getAppName(), instanceInfo.getInstanceId());

        sendNotice(InstanceInfo.builder()
                .instanceId(instanceInfo.getInstanceId())
                .serviceId(instanceInfo.getVIPAddress())
                .instanceStatus(instanceStatus)
                .build(), eventType);
    }


    private void sendNotice(InstanceInfo instanceInfo, String eventType) {
        try {
            communicateClient.noticeInstanceInfo(instanceInfo);
        } catch (Exception e) {
            log.error("发送实例{}消息失败,serviceId:{}, instanceId:{}",
                    eventType, instanceInfo.getServiceId(), instanceInfo.getInstanceId(), e);
        }
    }


}
