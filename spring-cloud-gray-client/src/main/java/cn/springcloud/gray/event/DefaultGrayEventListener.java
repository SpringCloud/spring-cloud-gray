package cn.springcloud.gray.event;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.InstanceLocalInfoAware;
import cn.springcloud.gray.exceptions.EventException;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.track.CommunicableGrayTrackHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class DefaultGrayEventListener implements GrayEventListener, InstanceLocalInfoAware {

    private CommunicableGrayManager grayManager;
    private CommunicableGrayTrackHolder grayTrackHolder;
    private InstanceLocalInfo instanceLocalInfo;

    private Map<SourceType, Consumer<GrayEventMsg>> handers = new HashMap<>();

    public DefaultGrayEventListener(CommunicableGrayTrackHolder grayTrackHolder, CommunicableGrayManager grayManager) {
        this.grayManager = grayManager;
        initHandlers();
        this.grayTrackHolder = grayTrackHolder;
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
        if (!StringUtils.equals(msg.getServiceId(), instanceLocalInfo.getServiceId())) {
            return;
        }
        if (StringUtils.isNotEmpty(msg.getInstanceId())
                && !StringUtils.equals(msg.getInstanceId(), instanceLocalInfo.getInstanceId())) {
            return;
        }

        GrayTrackDefinition definition = (GrayTrackDefinition) msg.getExtra();
        if (definition == null) {
            List<GrayTrackDefinition> definitions =
                    grayTrackHolder.getGrayInformationClient().getTrackDefinitions(msg.getServiceId(), msg.getInstanceId());
            if (definitions != null) {
                definitions.forEach(d -> {
                    grayTrackHolder.updateTrackDefinition(d);
                });
            }
        } else {
            switch (msg.getEventType()) {
                case DOWN:
                    grayTrackHolder.deleteTrackDefinition(definition.getName());
                case UPDATE:
                    grayTrackHolder.updateTrackDefinition(definition);
            }
        }


    }

    @Override
    public void setInstanceLocalInfo(InstanceLocalInfo instanceLocalInfo) {
        this.instanceLocalInfo = instanceLocalInfo;
    }
}
