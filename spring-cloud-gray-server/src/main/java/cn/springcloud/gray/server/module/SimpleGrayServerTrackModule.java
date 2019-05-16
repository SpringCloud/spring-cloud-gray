package cn.springcloud.gray.server.module;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import cn.springcloud.gray.server.service.GrayTrackService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class SimpleGrayServerTrackModule implements GrayServerTrackModule {

    private GrayEventPublisher grayEventPublisher;
    private GrayTrackService grayTrackService;

    public SimpleGrayServerTrackModule(GrayEventPublisher grayEventPublisher, GrayTrackService grayTrackService) {
        this.grayEventPublisher = grayEventPublisher;
        this.grayTrackService = grayTrackService;
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
        GrayTrack grayTrack = grayTrackService.findOneModel(id);
        grayTrackService.delete(id);
        publishGrayTrackEvent(EventType.DOWN, grayTrack);
    }

    @Override
    public void saveGrayTrack(GrayTrack track) {
        GrayTrack pre = null;
        if (track.getId() != null) {
            pre = grayTrackService.findOneModel(track.getId());
        }
        grayTrackService.saveModel(track);
        if (pre != null) {
            if (!StringUtils.equals(pre.getServiceId(), track.getServiceId()) ||
                    !StringUtils.equals(pre.getInstanceId(), track.getInstanceId())) {
                publishGrayTrackEvent(EventType.DOWN, pre);
            }
        }
        publishGrayTrackEvent(EventType.UPDATE, track);
    }

    protected void publishGrayTrackEvent(EventType eventType, GrayTrack grayTrack) {
        GrayEventMsg eventMsg = new GrayEventMsg();
        eventMsg.setInstanceId(grayTrack.getInstanceId());
        eventMsg.setServiceId(grayTrack.getServiceId());
        eventMsg.setEventType(eventType);
        eventMsg.setSourceType(SourceType.GRAY_TRACK);
        GrayTrackDefinition definition = new GrayTrackDefinition();
        definition.setName(grayTrack.getName());
        definition.setValue(grayTrack.getInfos());
        eventMsg.setExtra(definition);
        publishGrayEvent(eventMsg);
    }

    protected void publishGrayEvent(GrayEventMsg eventMsg) {
        grayEventPublisher.publishEvent(eventMsg);
    }
}
