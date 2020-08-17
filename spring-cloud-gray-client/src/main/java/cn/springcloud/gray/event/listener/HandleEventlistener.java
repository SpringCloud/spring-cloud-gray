package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.changed.notify.ChangedType;
import cn.springcloud.gray.handle.HandleInfo;
import cn.springcloud.gray.handle.HandleManager;
import cn.springlcoud.gray.event.HandleEvent;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 16:41
 */
public class HandleEventlistener extends AbstractGrayEventListener<HandleEvent> {

    private HandleManager handleManager;

    public HandleEventlistener(HandleManager handleManager) {
        this.handleManager = handleManager;
    }

    @Override
    protected void onUpdate(HandleEvent event) {
        HandleInfo handleInfo = handleManager.getHandleInfo(event.getHandleId());
        if (Objects.isNull(handleInfo)) {
            handleInfo = new HandleInfo(event.getHandleId(), event.getType());
            handleManager.addHandleInfo(handleInfo);
        } else {
            handleInfo.setType(event.getType());
            handleManager.publishHandleInfoChanged(ChangedType.UPDATED, handleInfo);
        }
    }

    @Override
    protected void onDelete(HandleEvent event) {
        handleManager.removeHandleInfo(event.getHandleId());
    }
}
