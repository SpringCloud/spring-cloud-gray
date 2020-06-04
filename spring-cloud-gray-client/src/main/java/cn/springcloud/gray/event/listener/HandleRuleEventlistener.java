package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springlcoud.gray.event.HandleRuleEvent;

/**
 * @author saleson
 * @date 2020-05-31 16:41
 */
public class HandleRuleEventlistener extends AbstractGrayEventListener<HandleRuleEvent> {

    private HandleRuleManager handleRuleManager;

    public HandleRuleEventlistener(HandleRuleManager handleRuleManager) {
        this.handleRuleManager = handleRuleManager;
    }

    @Override
    protected void onUpdate(HandleRuleEvent event) {
        handleRuleManager.putHandleRuleDefinition(event.getHandleRuleDefinition());
    }

    @Override
    protected void onDelete(HandleRuleEvent event) {
        handleRuleManager.removeHandleRuleInfo(event.getSourceId());
    }
}
