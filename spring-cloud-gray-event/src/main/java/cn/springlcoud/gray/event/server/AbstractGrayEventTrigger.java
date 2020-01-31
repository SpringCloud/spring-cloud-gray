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
    private List<EventConverter> eventConverters;
    private GenericRetriever<EventConverter> genericRetriever;


    public AbstractGrayEventTrigger(GrayEventSender grayEventSender, List<EventConverter> eventConverters) {
        this.grayEventSender = grayEventSender;
        this.eventConverters = eventConverters;
        this.genericRetriever = new GenericRetriever<>(eventConverters, EventConverter.class);
    }

    @Override
    public void triggering(Object eventMsg, TriggerType triggerType) {
        GrayEvent grayEvent = convertGrayEvent(eventMsg, triggerType);
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
