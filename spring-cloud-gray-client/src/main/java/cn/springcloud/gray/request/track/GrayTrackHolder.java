package cn.springcloud.gray.request.track;

import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import cn.springcloud.gray.utils.LogUtils;

import java.util.Collection;
import java.util.List;

public interface GrayTrackHolder {


    List<GrayInfoTracker> getGrayInfoTrackers();


    Collection<GrayTrackDefinition> getTrackDefinitions();

    GrayTrackDefinition getGrayTrackDefinition(String name);

    void updateTrackDefinition(GrayTrackDefinition definition);

    void deleteTrackDefinition(String name);


    default <REQ> void recordGrayTrack(GrayTrackInfo info, REQ req) {
        getGrayInfoTrackers().forEach(tracker -> {
            GrayTrackDefinition definition = getGrayTrackDefinition(tracker.name());
            if (definition != null) {
                try {
                    TrackArgs args = TrackArgs.builder()
                            .trackInfo(info)
                            .request(req)
                            .trackDefinition(definition)
                            .build();
                    tracker.call(args);
                } catch (Exception e) {
                    LogUtils.logger(GrayTrackHolder.class).error(e.getMessage());
                }
            }
        });
    }

}
