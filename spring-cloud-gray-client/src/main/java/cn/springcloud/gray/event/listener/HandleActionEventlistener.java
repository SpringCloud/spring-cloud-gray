package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.event.HandleActionEvent;

/**
 * @author saleson
 * @date 2020-05-31 16:41
 */
public class HandleActionEventlistener extends AbstractGrayEventListener<HandleActionEvent> {

    private HandleManager handleManager;

    public HandleActionEventlistener(HandleManager handleManager) {
        this.handleManager = handleManager;
    }

    @Override
    protected void onUpdate(HandleActionEvent event) {
        handleManager.addHandleActionDefinition(event.getHandleId(), event.getHandleActionDefinition());
    }

    @Override
    protected void onDelete(HandleActionEvent event) {
        handleManager.removeHandleActionDefinition(event.getHandleId(), event.getHandleActionDefinition().getId());
    }
}
