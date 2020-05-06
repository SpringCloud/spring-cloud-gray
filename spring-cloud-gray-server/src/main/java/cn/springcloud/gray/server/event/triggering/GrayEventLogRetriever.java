package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.retriever.GenericRetriever;
import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import cn.springlcoud.gray.event.*;
import cn.springlcoud.gray.event.codec.GrayEventDecoder;
import cn.springlcoud.gray.event.server.EventConverter;
import cn.springlcoud.gray.event.server.GrayEventRetriever;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private Map<String, Class<? extends GrayEvent>> grayEventTypeClsMappings = new HashMap<>();


    public GrayEventLogRetriever(
            GrayEventLogModule grayEventLogModule,
            List<EventConverter> eventConverters,
            GrayEventDecoder<String> grayEventDecoder) {
        this.grayEventLogModule = grayEventLogModule;
        this.genericRetriever = new GenericRetriever<>(eventConverters, EventConverter.class, 1);
        this.grayEventDecoder = grayEventDecoder;
        initEventTypeClsMappings();
    }

    @Override
    public GrayEventRetrieveResult retrieveGreaterThan(long sortMark) {
        List<GrayEventLog> grayEventLogs = queryAllGreaterThanSortMark(sortMark);
        List<GrayEvent> grayEvents = toGrayEvents(grayEventLogs);
        return new GrayEventRetrieveResult(grayEvents);
    }

    @Override
    public Class<? extends GrayEvent> retrieveTypeClass(String type) {
        return grayEventTypeClsMappings.get(type);
    }


    @Override
    public long getNewestSortMark() {
        return grayEventLogModule.getNewestSortMark();
    }


    private void initEventTypeClsMappings() {
        grayEventTypeClsMappings.put(GrayServiceEvent.class.getSimpleName(), GrayServiceEvent.class);
        grayEventTypeClsMappings.put(GrayInstanceEvent.class.getSimpleName(), GrayInstanceEvent.class);
        grayEventTypeClsMappings.put(GrayPolicyEvent.class.getSimpleName(), GrayPolicyEvent.class);
        grayEventTypeClsMappings.put(GrayDecisionEvent.class.getSimpleName(), GrayDecisionEvent.class);
        grayEventTypeClsMappings.put(GrayTrackEvent.class.getSimpleName(), GrayTrackEvent.class);
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
//            Class<? extends GrayEvent> eventClass =
//                    (Class<? extends GrayEvent>) Class.forName(grayEventLog.getEventClass());
            Class<? extends GrayEvent> eventClass = retrieveTypeClass(grayEventLog.getType());
            grayEvent = grayEventDecoder.decode(grayEventLog.getContent(), eventClass);
        } catch (IOException e) {
            //todo print error log
            log.error("");
            throw new RuntimeException(e);
        }
        return decorateEvent(grayEvent);
    }


    protected GrayEvent decorateEvent(GrayEvent grayEvent) {
        EventConverter eventConverter = genericRetriever.retrieve(grayEvent.getClass());
        if (Objects.isNull(eventConverter)) {
            log.warn("没有找到EventConverter, GrayEvent class：{}", grayEvent.getClass());
            return grayEvent;
        }
        return eventConverter.decorate(grayEvent);
    }


}
