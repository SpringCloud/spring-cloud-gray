package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.PolicyDefinition;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.event.GrayPolicyEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

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
        return event;
    }
}
