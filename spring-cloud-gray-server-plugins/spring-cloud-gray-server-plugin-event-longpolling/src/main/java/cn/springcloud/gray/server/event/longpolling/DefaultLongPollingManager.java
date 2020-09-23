package cn.springcloud.gray.server.event.longpolling;

import cn.springcloud.gray.server.event.longpolling.configuration.properties.EventLongPollingProperties;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.longpolling.ListenResult;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author saleson
 * @date 2020-02-03 22:52
 */
public class DefaultLongPollingManager implements LongPollingManager {

    private EventLongPollingProperties eventLongPollingProperties;
    private Queue<ClientLongPolling> allSubs = new ConcurrentLinkedQueue<ClientLongPolling>();


    public DefaultLongPollingManager(EventLongPollingProperties eventLongPollingProperties) {
        this.eventLongPollingProperties = eventLongPollingProperties;
    }

    @Override
    public int sendEvent(GrayEvent grayEvent) {
        int sendedCount = 0;
        long eventSortMark = grayEvent.getSortMark();
        for (Iterator<ClientLongPolling> iter = allSubs.iterator(); iter.hasNext(); ) {
            ClientLongPolling clientLongPolling = iter.next();
            if (clientLongPolling.getSortMark() < eventSortMark) {
                clientLongPolling.getDeferredResult().setResult(createListenResult(ListenResult.RESULT_STATUS_HAS_NEWER));
                iter.remove();
                sendedCount++;
            }
        }
        return sendedCount;
    }


    @Override
    public void mount(ClientLongPolling clientLongPolling) {
        DeferredResult<ListenResult> result = clientLongPolling.getDeferredResult();
        result.onTimeout(() -> {
            result.setResult(createListenResult(ListenResult.RESULT_STATUS_TIMEOUT));
            allSubs.remove(clientLongPolling);
        });
        allSubs.add(clientLongPolling);
    }

    @Override
    public void unmount(ClientLongPolling clientLongPolling) {
        allSubs.remove(clientLongPolling);
    }

    @Override
    public long getTimeout(Long clientTimeout) {
        if (Objects.isNull(clientTimeout)) {
            return eventLongPollingProperties.getDefaultTimeoutMs();
        }
        if (clientTimeout < 5000) {
            return 5000;
        }
        return clientTimeout;
    }

    private ListenResult createListenResult(int status) {
        ListenResult listenResult = new ListenResult();
        listenResult.setStatus(status);
        return listenResult;
    }
}
