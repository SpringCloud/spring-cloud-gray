package cn.springcloud.gray.request.track;

import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;

import java.util.List;

public abstract class AbstractCommunicableGrayTrackHolder extends SimpleGrayTrackHolder implements CommunicableGrayTrackHolder {


    private InformationClient informationClient;

    public AbstractCommunicableGrayTrackHolder(
            InformationClient informationClient,
            List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers,
            List<GrayTrackDefinition> trackDefinitions) {
        super(trackers, trackDefinitions);
        this.informationClient = informationClient;
    }

    @Override
    public InformationClient getGrayInformationClient() {
        return informationClient;
    }
}
