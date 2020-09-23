package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.event.GrayTrackEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

/**
 * @author saleson
 * @date 2020-02-05 14:27
 */
public class GraTrackEventConverter extends AbstrctEventConverter<GrayTrack, GrayTrackEvent> {
    @Override
    protected GrayTrackEvent convertDeleteData(GrayTrack grayTrack) {
        return toGrayTrackEvent(grayTrack);
    }

    @Override
    protected GrayTrackEvent convertModifyData(GrayTrack grayTrack) {
        return toGrayTrackEvent(grayTrack);
    }


    private GrayTrackEvent toGrayTrackEvent(GrayTrack grayTrack) {
        GrayTrackEvent event = new GrayTrackEvent();
        event.setServiceId(grayTrack.getServiceId());
        event.setInstanceId(grayTrack.getInstanceId());
        event.setSourceId(String.valueOf(grayTrack.getId()));
        GrayTrackDefinition trackDefinition = new GrayTrackDefinition();
        trackDefinition.setName(grayTrack.getName());
        trackDefinition.setValue(grayTrack.getInfos());
        event.setSource(trackDefinition);
        return event;
    }
}
