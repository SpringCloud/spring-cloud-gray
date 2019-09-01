package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;

import java.util.Objects;

public class GrayInstanceEventSourceConverter extends GrayModuleAwareEventSourceConverter {



    @Override
    public Object convert(EventType eventType, SourceType sourceType, Object source) {
        if(!Objects.equals(sourceType,SourceType.GRAY_INSTANCE)){
            return null;
        }

        if(Objects.equals(eventType, EventType.DOWN) || source==null){
           return null;
        }

        GrayInstance grayInstance = (GrayInstance) source;
        return grayModule.getGrayInstance(grayInstance.getServiceId(), grayInstance.getInstanceId());
    }
}
