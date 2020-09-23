package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.event.GrayInstanceEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

/**
 * @author saleson
 * @date 2020-02-05 13:34
 */
public class GrayInstanceEventConverter extends AbstrctEventConverter<GrayInstance, GrayInstanceEvent> {

    private GrayModule grayModule;

    public GrayInstanceEventConverter(GrayModule grayModule) {
        this.grayModule = grayModule;
    }

    @Override
    protected GrayInstanceEvent convertDeleteData(GrayInstance instance) {
        GrayInstanceEvent event = new GrayInstanceEvent();
        cn.springcloud.gray.model.GrayInstance grayInstance = grayModule.ofGrayInstance(instance);
        event.setSource(grayInstance);
        return event;
    }

    @Override
    protected GrayInstanceEvent convertModifyData(GrayInstance instance) {
        GrayInstanceEvent event = new GrayInstanceEvent();
        cn.springcloud.gray.model.GrayInstance grayInstance =
                grayModule.getGrayInstance(instance.getServiceId(), instance.getInstanceId());
        event.setSource(grayInstance);
        return event;
    }


}
