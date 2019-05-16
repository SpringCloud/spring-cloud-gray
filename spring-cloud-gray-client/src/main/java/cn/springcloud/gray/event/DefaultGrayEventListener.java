package cn.springcloud.gray.event;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.exceptions.EventException;
import cn.springcloud.gray.model.GrayInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class DefaultGrayEventListener implements GrayEventListener {

    private CommunicableGrayManager grayManager;

    private Map<SourceType, Consumer<GrayEventMsg>> handers = new HashMap<>();

    public DefaultGrayEventListener(CommunicableGrayManager grayManager) {
        this.grayManager = grayManager;
    }

    @Override
    public void onEvent(GrayEventMsg msg) throws EventException {
        handleSource(msg);
    }

    private void handleSource(GrayEventMsg msg) {
        Optional.ofNullable(getHandler(msg.getSourceType())).ifPresent(handler -> {
            handler.accept(msg);
        });
    }


    private Consumer<GrayEventMsg> getHandler(SourceType type) {
        return handers.get(type);
    }


    private void initHandlers() {
        putHandler(SourceType.GRAY_INSTANCE, this::handleGrayInstance)
                .putHandler(SourceType.GRAY_TRACK, this::handleGrayTrack);
    }

    private DefaultGrayEventListener putHandler(SourceType sourceType, Consumer<GrayEventMsg> handler) {
        handers.put(sourceType, handler);
        return this;
    }


    private void handleGrayInstance(GrayEventMsg msg) {
        switch (msg.getEventType()) {
            case DOWN:
                grayManager.closeGray(msg.getServiceId(), msg.getInstanceId());
            case UPDATE:
                GrayInstance grayInstance = grayManager.getGrayInformationClient()
                        .getGrayInstance(msg.getServiceId(), msg.getInstanceId());
                grayManager.updateGrayInstance(grayInstance);
        }
    }

    private void handleGrayTrack(GrayEventMsg msg) {
        //todo

    }
}
