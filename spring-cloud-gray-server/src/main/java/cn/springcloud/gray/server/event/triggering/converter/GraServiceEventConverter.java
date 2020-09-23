package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.event.GrayServiceEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

/**
 * @author saleson
 * @date 2020-02-05 14:27
 */
public class GraServiceEventConverter extends AbstrctEventConverter<GrayService, GrayServiceEvent> {
    @Override
    protected GrayServiceEvent convertDeleteData(GrayService grayService) {
        return toGrayServiceEvent(grayService);
    }

    @Override
    protected GrayServiceEvent convertModifyData(GrayService grayService) {
        return toGrayServiceEvent(grayService);
    }


    private GrayServiceEvent toGrayServiceEvent(GrayService grayService) {
        GrayServiceEvent event = new GrayServiceEvent();
        event.setServiceId(grayService.getServiceId());
        return event;
    }
}
