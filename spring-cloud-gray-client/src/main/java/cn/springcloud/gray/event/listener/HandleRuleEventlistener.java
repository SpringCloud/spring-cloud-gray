package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springlcoud.gray.event.HandleRuleEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2020-05-31 16:41
 */
@Slf4j
public class HandleRuleEventlistener extends AbstractGrayEventListener<HandleRuleEvent> {

    private HandleRuleManager handleRuleManager;

    public HandleRuleEventlistener(HandleRuleManager handleRuleManager) {
        this.handleRuleManager = handleRuleManager;
    }

    @Override
    protected void onUpdate(HandleRuleEvent event) {
        InstanceLocalInfo instanceLocalInfo = GrayClientHolder.getInstanceLocalInfo();
        if (!StringUtils.equals(instanceLocalInfo.getServiceId(), event.getModuleId())) {
            log.info("HandleRuleEvent.moduleId!=instanceLocalInfo.serviceId, HandleRuleEvent - 6 : {}", event);
            onDelete(event);
            return;
        }
        if (!StringUtils.equals(instanceLocalInfo.getServiceId(), event.getResource()) &&
                !StringUtils.equals(instanceLocalInfo.getInstanceId(), event.getResource())) {
            log.info("HandleRuleEvent.resource ont in [instanceLocalInfo.serviceId, instanceLocalInfo.instanceId], HandleRuleEvent - 6 : {}", event);
            onDelete(event);
            return;
        }
        handleRuleManager.putHandleRuleDefinition(event.getHandleRuleDefinition());
    }

    @Override
    protected void onDelete(HandleRuleEvent event) {
        handleRuleManager.removeHandleRuleInfo(event.getSourceId());
    }
}
