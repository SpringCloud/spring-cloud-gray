package cn.springcloud.gray.event.server;

import cn.springcloud.gray.concurrent.DefaultThreadFactory;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.keeper.ListKeeper;
import cn.springcloud.gray.keeper.SyncListKeeper;
import cn.springcloud.gray.retriever.GenericRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-01-31 14:45
 */
@Slf4j
public abstract class AbstractGrayEventTrigger implements GrayEventTrigger {

    private GrayEventSender grayEventSender;
    private GenericRetriever<EventConverter> genericRetriever;
    private ListKeeper<GrayEventObserver> grayEventObservers = new SyncListKeeper<>();
    private ExecutorService executorService;
    private long triggerDelayMills = 20;

    public AbstractGrayEventTrigger(GrayEventSender grayEventSender) {
        this(grayEventSender, null,
                new ThreadPoolExecutor(10, 50, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new DefaultThreadFactory("event-trigger")));
    }

    public AbstractGrayEventTrigger(GrayEventSender grayEventSender, List<EventConverter> eventConverters, ExecutorService executorService) {
        this.grayEventSender = grayEventSender;
        this.executorService = executorService;
        if (!Objects.isNull(eventConverters)) {
            createEventConverterRetriever(eventConverters);
        }
    }


    @Override
    public void triggering(Object eventSource, TriggerType triggerType) {
        executorService.execute(() -> {
            long triggerDelay = getTriggerDelayMills();
            if (triggerDelay > 0) {
                try {
                    Thread.sleep(triggerDelay);
                } catch (InterruptedException e) {
                }
            }
            innerTriggering(eventSource, triggerType);
        });
    }

    protected abstract void logEventTrigger(Object eventSource, TriggerType triggerType, GrayEvent grayEvent);


    public void createEventConverterRetriever(List<EventConverter> eventConverters) {
        this.genericRetriever = new GenericRetriever<>(eventConverters, EventConverter.class);
    }


    public void addObserver(GrayEventObserver observer) {
        grayEventObservers.add(observer);
    }

    public void removeObserver(GrayEventObserver observer) {
        grayEventObservers.remove(observer);
    }

    public List<GrayEventObserver> getObservers() {
        return grayEventObservers.values();
    }

    protected void innerTriggering(Object eventSource, TriggerType triggerType) {
        GrayEvent grayEvent = convertGrayEvent(eventSource, triggerType);
        if (Objects.isNull(grayEvent)) {
//            log.warn("转换失败, grayEvent is null, eventSource:{}, triggerType:{}", eventSource, triggerType);
            return;
        }

        if (Objects.isNull(grayEvent.getTriggerType())) {
            grayEvent.setTriggerType(triggerType);
        }

        noticeObservers(GrayEventObserveState.CREATED, grayEvent);

//        noticeObservers(GrayEventObserveState.READY_FOR_SENDING, grayEvent);
        grayEventSender.send(grayEvent);

        noticeObservers(GrayEventObserveState.SENT, grayEvent);
    }

    protected void noticeObservers(GrayEventObserveState observeState, GrayEvent grayEvent) {
        getObservers().forEach(observer -> observer.observe(observeState, grayEvent));
    }

    protected GrayEvent convertGrayEvent(Object eventSource, TriggerType triggerType) {
        EventConverter eventConverter = getEventConverter(eventSource.getClass());
        GrayEvent event = eventConverter.convert(eventSource, triggerType);
        if (Objects.isNull(event)) {
            return null;
        }
        logEventTrigger(eventSource, triggerType, event);
        return eventConverter.decorate(event);
    }


    protected EventConverter getEventConverter(Class<?> sourceCls) {
        EventConverter eventConverter = genericRetriever.retrieve(sourceCls);
        if (Objects.isNull(eventConverter)) {
            log.error("没有找到支持 '{}' 的EventConverter", sourceCls);
            throw new NullPointerException("has no EventConverter support");
        }
        return eventConverter;
    }


    public void setTriggerDelayMills(long triggerDelayMills) {
        this.triggerDelayMills = triggerDelayMills;
    }

    public long getTriggerDelayMills() {
        return triggerDelayMills;
    }
}
