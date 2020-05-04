package cn.springcloud.gray.request.track;

import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DefaultGrayTrackHolder extends SimpleGrayTrackHolder {


    public DefaultGrayTrackHolder(
            List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers,
            List<GrayTrackDefinition> trackDefinitions) {
        super(trackers, trackDefinitions);
    }

}
