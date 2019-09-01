package cn.springcloud.gray.server.event;

import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.SourceType;

public interface EventSourceConverter {


    Object convert(EventType eventType, SourceType sourceType, Object source);
}
