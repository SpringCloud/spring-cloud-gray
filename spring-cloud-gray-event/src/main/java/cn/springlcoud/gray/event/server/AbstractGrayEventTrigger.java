package cn.springlcoud.gray.event.server;

import cn.springcloud.gray.retriever.GenericRetriever;
import cn.springlcoud.gray.event.GrayEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-31 14:45
 */
@Slf4j
public abstract class AbstractGrayEventTrigger implements GrayEventTrigger {

    private GrayEventSender grayEventSender;
    private GenericRetriever<EventConverter> genericRetriever;


    public AbstractGrayEventTrigger(GrayEventSender grayEventSender, List<EventConverter> eventConverters) {
        this.grayEventSender = grayEventSender;
        this.genericRetriever = new GenericRetriever<>(eventConverters, EventConverter.class);
    }

    @Override
    public void triggering(Object eventMsg, TriggerType triggerType) {
        GrayEvent grayEvent = convertGrayEvent(eventMsg, triggerType);
        if (Objects.isNull(grayEvent)) {
            log.warn("转换失败, grayEvent is null, eventMsg:{}, triggerType:{}", eventMsg, triggerType);
            return;
        }
        logEventTrigger(eventMsg, triggerType, grayEvent);
        grayEventSender.send(grayEvent);
    }


    protected abstract void logEventTrigger(Object eventMsg, TriggerType triggerType, GrayEvent grayEvent);

    protected GrayEvent convertGrayEvent(Object eventMsg, TriggerType triggerType) {
        EventConverter eventConverter = getEventConverter(eventMsg.getClass());
        return eventConverter.convert(eventMsg, triggerType);
    }


    protected EventConverter getEventConverter(Class<?> sourceCls) {
        EventConverter eventConverter = genericRetriever.retrieve(sourceCls);
        if (Objects.isNull(eventConverter)) {
            log.error("没有找到支持 '{}' 的EventConverter", sourceCls);
            throw new NullPointerException("has no EventConverter support");
        }
        return eventConverter;
    }


}
