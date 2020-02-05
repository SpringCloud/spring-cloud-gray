package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springlcoud.gray.event.GrayPolicyEvent;
import cn.springlcoud.gray.event.server.AbstrctEventConverter;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-05 14:06
 */
public class GrayPolicyEventConverter extends AbstrctEventConverter<GrayPolicy, GrayPolicyEvent> {

    private GrayModule grayModule;

    public GrayPolicyEventConverter(GrayModule grayModule) {
        this.grayModule = grayModule;
    }


    @Override
    protected GrayPolicyEvent convertDeleteData(GrayPolicy grayPolicy) {
        GrayPolicyEvent event = toGrayPolicyEvent(grayPolicy);
        PolicyDefinition definition = grayModule.ofGrayPolicy(grayPolicy);
        event.setSource(definition);
        return event;
    }

    @Override
    protected GrayPolicyEvent convertModifyData(GrayPolicy grayPolicy) {
        GrayPolicyEvent event = toGrayPolicyEvent(grayPolicy);
        PolicyDefinition definition = grayModule.ofGrayPolicy(grayPolicy);
        definition.setList(grayModule.ofGrayDecisionByPolicyId(grayPolicy.getId()));
        event.setSource(definition);
        return event;
    }

    private GrayPolicyEvent toGrayPolicyEvent(GrayPolicy grayPolicy) {
        GrayPolicyEvent event = new GrayPolicyEvent();
        GrayInstance grayInstance = grayModule.getGrayServerModule().getGrayInstance(grayPolicy.getInstanceId());
        if (Objects.isNull(grayInstance)) {
            return null;
        }
        event.setInstanceId(grayInstance.getInstanceId());
        event.setServiceId(grayInstance.getServiceId());
        return event;
    }
}
