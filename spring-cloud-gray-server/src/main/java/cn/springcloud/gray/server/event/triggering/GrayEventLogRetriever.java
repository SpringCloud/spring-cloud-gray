package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.retriever.GenericRetriever;
import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import cn.springlcoud.gray.event.GrayEvent;
import cn.springlcoud.gray.event.server.EventConverter;
import cn.springlcoud.gray.event.server.GrayEventRetrieveResult;
import cn.springlcoud.gray.event.server.GrayEventRetriever;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-02-01 16:38
 */
@Slf4j
public class GrayEventLogRetriever implements GrayEventRetriever {

    private GrayEventLogModule grayEventLogModule;
    private GenericRetriever<EventConverter> genericRetriever;
    private GrayEventDecoder<String> grayEventDecoder;


    public GrayEventLogRetriever(
            GrayEventLogModule grayEventLogModule,
            List<EventConverter> eventConverters,
            GrayEventDecoder<String> grayEventDecoder) {
        this.grayEventLogModule = grayEventLogModule;
        this.genericRetriever = new GenericRetriever<>(eventConverters, EventConverter.class, 1);
        this.grayEventDecoder = grayEventDecoder;
    }

    @Override
    public GrayEventRetrieveResult retrieveGreaterThan(long sortMark) {
        List<GrayEventLog> grayEventLogs = queryAllGreaterThanSortMark(sortMark);
        List<GrayEvent> grayEvents = toGrayEvents(grayEventLogs);
        return new GrayEventRetrieveResult(grayEvents);
    }


    private List<GrayEventLog> queryAllGreaterThanSortMark(long sortMark) {
        return grayEventLogModule.queryAllGreaterThanSortMark(sortMark);
    }


    private List<GrayEvent> toGrayEvents(List<GrayEventLog> grayEventLogs) {
        return grayEventLogs.stream().map(this::toGrayEvent).collect(Collectors.toList());
    }


    private GrayEvent toGrayEvent(GrayEventLog grayEventLog) {
        GrayEvent grayEvent = null;
        try {
            Class<? extends GrayEvent> eventClass =
                    (Class<? extends GrayEvent>) Class.forName(grayEventLog.getEventClass());
            grayEvent = grayEventDecoder.decode(grayEventLog.getContent(), eventClass);
        } catch (ClassNotFoundException | IOException e) {
            log.error("");
            throw new RuntimeException(e);
        }
        return decorateEvent(grayEvent);
    }


    protected GrayEvent decorateEvent(GrayEvent grayEvent) {
        List<EventConverter> eventConverters = genericRetriever.retrieveFunctions(grayEvent.getClass());
        GrayEvent event = grayEvent;
        for (EventConverter eventConverter : eventConverters) {
            event = eventConverter.decorate(event);
        }
        return event;
    }


}
