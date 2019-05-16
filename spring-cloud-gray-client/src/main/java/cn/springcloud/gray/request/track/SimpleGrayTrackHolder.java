package cn.springcloud.gray.request.track;

import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.GrayInfoTracker;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.springframework.core.OrderComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleGrayTrackHolder implements GrayTrackHolder {

    private List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers = new ArrayList<>();

    private Map<String, GrayTrackDefinition> trackDefinitions = new ConcurrentHashMap<>();


    public SimpleGrayTrackHolder(List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers, List<GrayTrackDefinition> trackDefinitions) {
        initGrayInfoTrackers(trackers);
        initGrayTrackDefinitions(trackDefinitions);
    }

    private void initGrayTrackDefinitions(Collection<GrayTrackDefinition> trackDefinitions) {
        if (trackDefinitions != null) {
            trackDefinitions.forEach(definition -> {
                if (!this.trackDefinitions.containsKey(definition.getName())) {
                    this.trackDefinitions.put(definition.getName(), definition);
                }
            });
        }
    }

    private void initGrayInfoTrackers(List<GrayInfoTracker<? extends GrayTrackInfo, ?>> trackers) {
        if (trackers == null) {
            return;
        }
        OrderComparator.sort(trackers);
        this.trackers = trackers;
    }


    @Override
    public List<GrayInfoTracker> getGrayInfoTrackers() {
        return Collections.unmodifiableList(trackers);
    }

    @Override
    public Collection<GrayTrackDefinition> getTrackDefinitions() {
        return trackDefinitions.values();
    }

    @Override
    public GrayTrackDefinition getGrayTrackDefinition(String name) {
        return trackDefinitions.get(name);
    }

    @Override
    public void updateTrackDefinition(GrayTrackDefinition definition) {
        updateTrackDefinition(trackDefinitions, definition);
    }

    protected void updateTrackDefinition(Map<String, GrayTrackDefinition> trackDefinitions, GrayTrackDefinition definition) {
        trackDefinitions.put(definition.getName(), definition);
    }

    @Override
    public void deleteTrackDefinition(String name) {
        trackDefinitions.remove(name);
    }


    protected void setTrackDefinitions(Map<String, GrayTrackDefinition> trackDefinitions) {
        this.trackDefinitions = trackDefinitions;
    }
}
