package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.event.*;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.server.module.gray.GrayServerTrackModule;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.service.GrayTrackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class JPAGrayServerTrackModule implements GrayServerTrackModule {

    private GraySourceEventPublisher graySourceEventPublisher;
    private GrayTrackService grayTrackService;

    public JPAGrayServerTrackModule(GraySourceEventPublisher graySourceEventPublisher, GrayTrackService grayTrackService) {
        this.graySourceEventPublisher = graySourceEventPublisher;
        this.grayTrackService = grayTrackService;
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
        publishGrayTrackEvent(EventType.DOWN, grayTrack);
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
                publishGrayTrackEvent(EventType.DOWN, pre);
            }
        }
        publishGrayTrackEvent(EventType.UPDATE, track);
        return newRecord;
    }

    protected void publishGrayTrackEvent(EventType eventType, GrayTrack grayTrack) {
        GrayEventMsg eventMsg = new GrayEventMsg();
        eventMsg.setInstanceId(grayTrack.getInstanceId());
        eventMsg.setServiceId(grayTrack.getServiceId());
        eventMsg.setEventType(eventType);
        eventMsg.setSourceType(SourceType.GRAY_TRACK);
//        GrayTrackDefinition definition = new GrayTrackDefinition();
//        definition.setName(grayTrack.getName());
//        definition.setValue(grayTrack.getInfos());
//        eventMsg.setSource(definition);
//        publishGrayEvent(eventMsg);
        graySourceEventPublisher.asyncPublishEvent(eventMsg, grayTrack);
    }
}
