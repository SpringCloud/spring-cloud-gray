package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.DecisionDefinitionMsg;
import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.exceptions.EventException;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Objects;

public class GrayDecisionEventSourceConverter extends GrayModuleAwareEventSourceConverter {

    private static final Logger log = LoggerFactory.getLogger(GrayDecisionEventSourceConverter.class);


    @Override
    public Object convert(EventType eventType, SourceType sourceType, Object source) {
        if(!Objects.equals(sourceType, SourceType.GRAY_DECISION)){
            return null;
        }

        if(source==null){
            throw new NullPointerException("event msg source is null");
        }

        GrayDecision grayDecision = (GrayDecision) source;
        DecisionDefinition decisionDefinition = null;
        try {
            decisionDefinition = grayModule.ofGrayDecision(grayDecision);
        } catch (IOException e) {
            log.error("从GrayDecision转DecisionDefinition失败:{}", grayDecision);
            throw new EventException("从GrayDecision转DecisionDefinition失败", e);
        }
        DecisionDefinitionMsg decisionDefinitionMsg = new DecisionDefinitionMsg();
        decisionDefinitionMsg.setPolicyId(String.valueOf(grayDecision.getPolicyId()));
        decisionDefinitionMsg.setId(decisionDefinition.getId());
        if(!Objects.equals(eventType, EventType.DOWN)){
            decisionDefinitionMsg.setInfos(decisionDefinition.getInfos());
        }
        decisionDefinitionMsg.setName(decisionDefinition.getName());
        return decisionDefinitionMsg;
    }

}
