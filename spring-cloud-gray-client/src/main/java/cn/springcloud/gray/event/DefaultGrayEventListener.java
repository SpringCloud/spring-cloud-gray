package cn.springcloud.gray.event;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.exceptions.EventException;
import cn.springcloud.gray.model.GrayInstance;

public class DefaultGrayEventListener implements GrayEventListener {

    private CommunicableGrayManager grayManager;

    public DefaultGrayEventListener(CommunicableGrayManager grayManager) {
        this.grayManager = grayManager;
    }

    @Override
    public void onEvent(GrayEventMsg msg) throws EventException {
        switch (msg.getEventType()) {
            case DOWN:
                grayManager.closeGray(msg.getServiceId(), msg.getInstanceId());
            case UPDATE:
                GrayInstance grayInstance = grayManager.getGrayInformationClient()
                        .getGrayInstance(msg.getServiceId(), msg.getInstanceId());
                grayManager.updateGrayInstance(grayInstance);
        }
    }
}
