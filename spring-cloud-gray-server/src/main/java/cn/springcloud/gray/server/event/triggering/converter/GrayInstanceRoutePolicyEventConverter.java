package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springlcoud.gray.event.GrayInstanceRoutePolicyEvent;
import cn.springlcoud.gray.event.server.AbstrctEventConverter;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-07 11:50
 */
public class GrayInstanceRoutePolicyEventConverter extends AbstrctEventConverter<InstanceRoutePolicy, GrayInstanceRoutePolicyEvent> {

    private GrayServerModule grayServerModule;

    public GrayInstanceRoutePolicyEventConverter(GrayServerModule grayServerModule) {
        this.grayServerModule = grayServerModule;
    }

    @Override
    protected GrayInstanceRoutePolicyEvent convertDeleteData(InstanceRoutePolicy instanceRoutePolicy) {
        return toGrayInstanceRoutePolicyEvent(instanceRoutePolicy);
    }

    @Override
    protected GrayInstanceRoutePolicyEvent convertModifyData(InstanceRoutePolicy instanceRoutePolicy) {
        return toGrayInstanceRoutePolicyEvent(instanceRoutePolicy);
    }

    private GrayInstanceRoutePolicyEvent toGrayInstanceRoutePolicyEvent(InstanceRoutePolicy instanceRoutePolicy) {
        GrayInstance grayInstance = grayServerModule.getGrayInstance(instanceRoutePolicy.getInstanceId());
        if (Objects.isNull(grayInstance)) {
            return null;
        }
        GrayInstanceRoutePolicyEvent event = new GrayInstanceRoutePolicyEvent();
        event.setInstanceRoutePolicyId(instanceRoutePolicy.getPolicyId());
        event.setInstanceId(instanceRoutePolicy.getInstanceId());
        event.setServiceId(grayInstance.getServiceId());
        event.setPolicyId(String.valueOf(instanceRoutePolicy.getPolicyId()));
        return event;
    }
}
