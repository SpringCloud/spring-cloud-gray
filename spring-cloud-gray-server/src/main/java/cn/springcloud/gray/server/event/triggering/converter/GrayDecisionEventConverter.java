package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.exceptions.EventException;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import cn.springcloud.gray.event.GrayDecisionEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-02-05 14:16
 */
@Slf4j
public class GrayDecisionEventConverter extends AbstrctEventConverter<GrayDecision, GrayDecisionEvent> {

    private GrayModule grayModule;

    public GrayDecisionEventConverter(GrayModule grayModule) {
        this.grayModule = grayModule;
    }

    @Override
    protected GrayDecisionEvent convertDeleteData(GrayDecision grayDecision) {
        GrayDecisionEvent event = toGrayDecisionEvent(grayDecision);
        DecisionDefinition definition = new DecisionDefinition();
        definition.setId(String.valueOf(grayDecision.getId()));
        definition.setName(grayDecision.getName());
        event.setSource(definition);
        return event;
    }

    @Override
    protected GrayDecisionEvent convertModifyData(GrayDecision grayDecision) {
        GrayDecisionEvent event = toGrayDecisionEvent(grayDecision);
        try {
            DecisionDefinition definition = grayModule.ofGrayDecision(grayDecision);
            event.setSource(definition);
        } catch (IOException e) {
            log.error("从GrayDecision转DecisionDefinition失败:{}", grayDecision);
            throw new EventException("从GrayDecision转DecisionDefinition失败", e);
        }
        return event;
    }


    private GrayDecisionEvent toGrayDecisionEvent(GrayDecision grayDecision) {
        GrayDecisionEvent event = new GrayDecisionEvent();
        event.setPolicyId(String.valueOf(grayDecision.getPolicyId()));
        return event;
    }
}
