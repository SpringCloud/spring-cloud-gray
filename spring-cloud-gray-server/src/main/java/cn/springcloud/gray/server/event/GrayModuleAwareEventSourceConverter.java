package cn.springcloud.gray.server.event;

import cn.springcloud.gray.server.event.EventSourceConverter;
import cn.springcloud.gray.server.module.gray.GrayModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;

public abstract class GrayModuleAwareEventSourceConverter implements EventSourceConverter {
    @Autowired
    @Lazy
    protected GrayModule grayModule;


}
