package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.GrayServiceEvent;

/**
 * @author saleson
 * @date 2020-02-05 14:37
 */
public class GrayServiceEventListener extends AbstractGrayEventListener<GrayServiceEvent> {

    private UpdateableGrayManager grayManager;


    public GrayServiceEventListener(UpdateableGrayManager grayManager) {
        this.grayManager = grayManager;
    }

    @Override
    protected void onUpdate(GrayServiceEvent event) {

    }

    @Override
    protected void onDelete(GrayServiceEvent event) {
        grayManager.removeGrayService(event.getServiceId());
    }
}
