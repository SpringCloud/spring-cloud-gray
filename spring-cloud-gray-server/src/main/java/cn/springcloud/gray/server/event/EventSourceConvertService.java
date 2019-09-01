package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.SourceType;

import java.util.List;

public interface EventSourceConvertService {

    Object convert(EventType eventType, SourceType sourceType, Object source);


    public static class Default implements EventSourceConvertService{
        private List<EventSourceConverter> converters;

        public Default(List<EventSourceConverter> converters) {
            this.converters = converters;
        }

        @Override
        public Object convert(EventType eventType, SourceType sourceType, Object source) {
            for (EventSourceConverter converter : converters) {
                Object value = converter.convert(eventType, sourceType, source);
                if(value!=null){
                    return value;
                }
            }
            return null;
        }
    }

}
