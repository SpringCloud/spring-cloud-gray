package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.event.server.TriggerType;
import cn.springcloud.gray.server.module.gray.GrayServerTrackModule;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.service.GrayTrackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class JPAGrayServerTrackModule implements GrayServerTrackModule {

    private GrayEventTrigger grayEventTrigger;
    private GrayTrackService grayTrackService;

    public JPAGrayServerTrackModule(
            GrayEventTrigger grayEventTrigger,
            GrayTrackService grayTrackService) {
        this.grayEventTrigger = grayEventTrigger;
        this.grayTrackService = grayTrackService;
    }

    @Override
    public GrayTrack findFirstGrayTrack(String serviceId, String name) {
        return grayTrackService.findFirstGrayTrack(serviceId, name);
    }

    @Override
    public Page<GrayTrack> listGrayTracks(String serviceId, Pageable pageable) {
        return grayTrackService.listGrayTracks(serviceId, pageable);
    }

    @Override
    public Page<GrayTrack> listGrayTracks(Pageable pageable) {
        return grayTrackService.listGrayTracks(pageable);
    }

    @Override
    public List<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId) {
        return grayTrackService.listGrayTracksEmptyInstanceByServiceId(serviceId);
    }

    @Override
    public List<GrayTrack> listGrayTracksByInstanceId(String instanceId) {
        return grayTrackService.listGrayTracksByInstanceId(instanceId);
    }

    @Override
    public Page<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId, Pageable pageable) {
        return grayTrackService.listGrayTracksEmptyInstanceByServiceId(serviceId, pageable);
    }

    @Override
    public Page<GrayTrack> listGrayTracksByInstanceId(String instanceId, Pageable pageable) {
        return grayTrackService.listGrayTracksByInstanceId(instanceId, pageable);
    }

    @Override
    public void deleteGrayTrack(Long id) {
        GrayTrack grayTrack = getGrayTrack(id);
        grayTrackService.delete(id);
        triggerDeleteEvent(grayTrack);
    }

    @Override
    public GrayTrack getGrayTrack(Long id) {
        return grayTrackService.findOneModel(id);
    }

    @Override
    public GrayTrack saveGrayTrack(GrayTrack track) {
        GrayTrack pre = null;
        if (track.getId() != null) {
            pre = grayTrackService.findOneModel(track.getId());
        }
        GrayTrack newRecord = grayTrackService.saveModel(track);
        if (pre != null) {
            if (!StringUtils.equals(pre.getServiceId(), track.getServiceId()) ||
                    !StringUtils.equals(pre.getInstanceId(), track.getInstanceId())) {
                triggerDeleteEvent(pre);
            }
        }
        triggerUpdateEvent(track);
        return newRecord;
    }

    protected GrayEventTrigger getGrayEventTrigger() {
        return grayEventTrigger;
    }

    protected void triggerEvent(TriggerType triggerType, Object source) {
        getGrayEventTrigger().triggering(source, triggerType);
    }

    protected void triggerDeleteEvent(Object source) {
        triggerEvent(TriggerType.DELETE, source);
    }

    protected void triggerUpdateEvent(Object source) {
        triggerEvent(TriggerType.MODIFY, source);
    }

}
