package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.event.RoutePolicyEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-07 11:50
 */
public class GrayRoutePolicyEventConverter extends AbstrctEventConverter<RoutePolicyRecord, RoutePolicyEvent> {

    private GrayServerModule grayServerModule;

    public GrayRoutePolicyEventConverter(GrayServerModule grayServerModule) {
        this.grayServerModule = grayServerModule;
    }

    @Override
    protected RoutePolicyEvent convertDeleteData(RoutePolicyRecord routePolicyRecord) {
        return toGrayRoutePolicyEvent(routePolicyRecord);
    }

    @Override
    protected RoutePolicyEvent convertModifyData(RoutePolicyRecord routePolicyRecord) {
        if (Objects.equals(routePolicyRecord.getType(), RoutePolicyType.INSTANCE_ROUTE)
                && !grayServerModule.isActiveGrayInstance(routePolicyRecord.getResource())) {
            return null;
        }
        return toGrayRoutePolicyEvent(routePolicyRecord);
    }

    private RoutePolicyEvent toGrayRoutePolicyEvent(RoutePolicyRecord routePolicyRecord) {
        RoutePolicyEvent event = new RoutePolicyEvent();
        event.setRoutePolicy(routePolicyRecord.toRoutePolicy());
        return event;
    }
}
