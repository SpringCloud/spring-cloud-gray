package cn.springcloud.gray.event.sourcehander;

import cn.springcloud.gray.event.DecisionDefinitionMsg;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.model.PolicyDefinition;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SourceHanderService {

    void handle(GrayEventMsg eventMsg);


    public static class Default implements SourceHanderService {

        private List<GraySourceEventHandler> handlers;
        private Map<SourceType, Class> sourceTypeClassMap = new HashMap<>();
        private ObjectMapper objectMapper = new ObjectMapper();


        public Default(List<GraySourceEventHandler> handlers) {
            this.handlers = handlers;
            sourceTypeClassMap.put(SourceType.GRAY_INSTANCE, GrayInstance.class);
            sourceTypeClassMap.put(SourceType.GRAY_DECISION, DecisionDefinitionMsg.class);
            sourceTypeClassMap.put(SourceType.GRAY_POLICY, PolicyDefinition.class);
            sourceTypeClassMap.put(SourceType.GRAY_TRACK, GrayTrackDefinition.class);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        public void handle(GrayEventMsg eventMsg) {
            GrayEventMsg msg = convertSourceEventMsg(eventMsg);
            handlers.forEach(handler -> handler.handle(msg));
        }

        private GrayEventMsg convertSourceEventMsg(GrayEventMsg eventMsg) {
            Object source = eventMsg.getSource();
            if (source == null) {
                return eventMsg;
            }
            Class<?> sourceType = sourceTypeClassMap.get(eventMsg.getSourceType());
            if (sourceType == null) {
                return eventMsg;
            }
            if (sourceType.isInstance(source)) {
                return eventMsg;
            }
            eventMsg.setSource(objectMapper.convertValue(source, sourceType));
            return eventMsg;
        }
    }
}
