package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties.LongPollingProperties;
import cn.springcloud.gray.concurrent.DefaultThreadFactory;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springlcoud.gray.event.GrayEventRetrieveResult;
import cn.springlcoud.gray.event.client.GrayEventReceiver;
import cn.springlcoud.gray.event.longpolling.ListenResult;
import cn.springlcoud.gray.event.longpolling.domain.fo.LongpollingFO;
import cn.springlcoud.gray.event.longpolling.domain.fo.RetrieveFO;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-02-04 19:34
 */
@Slf4j
public class LongPollingWorker {

    private final ScheduledExecutorService executor;
    private final ScheduledExecutorService executorService;

    private final GrayEventReceiver grayEventReceiver;
    private LongPollingProperties longPollingProperties;
    private GrayEventRemoteClient grayEventRemoteClient;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    private volatile boolean stop = false;


    public LongPollingWorker(
            LongPollingProperties longPollingProperties,
            GrayEventRemoteClient grayEventRemoteClient,
            GrayEventReceiver grayEventReceiver,
            InstanceLocalInfoObtainer instanceLocalInfoObtainer) {

        this.longPollingProperties = longPollingProperties;
        this.grayEventRemoteClient = grayEventRemoteClient;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
        this.grayEventReceiver = grayEventReceiver;
        executor = Executors.newScheduledThreadPool(1,
                new DefaultThreadFactory("gray.client.event.longPolling"));

        executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                new DefaultThreadFactory("gray.client.event.retrieveResult.process"));
        execListening(100);
    }


    private void execListening() {
        execListening(1);
    }

    private void execListening(long delayMs) {
        executor.schedule(this::listenEvents, delayMs, TimeUnit.MILLISECONDS);
    }

    private void listenEvents() {
        if (isStop()) {
            return;
        }
        if (grayEventReceiver.getLocationNewestSortMark() < 0) {
            return;
        }
        ListenResult ls = null;
        try {
            ls = listening();
        } catch (Throwable e) {
            log.error("监听失败", e);
            execListening(1000);
        }

        if (!Objects.isNull(ls) && Objects.equals(ls.getStatus(), ListenResult.RESULT_STATUS_HAS_NEWER)) {
            executorService.execute(this::updateNewestEvent);
        } else {
            execListening();
        }
    }

    private void updateNewestEvent() {
        try {
            GrayEventRetrieveResult retrieveResult = retrieveNewestEvents();
            if (!Objects.isNull(retrieveResult)) {
                grayEventReceiver.receiveRetrieveResult(retrieveResult);
            }
        } catch (Throwable e) {
            log.error("更新最新的事件失败", e);
        }
        execListening();
    }


    private ListenResult listening() {
        LongpollingFO fo = new LongpollingFO();
        fo.setSortMark(grayEventReceiver.getLocationNewestSortMark());
        fo.setTimeout(longPollingProperties.getTimeout());
        InstanceLocalInfo instanceLocalInfo = getInstanceLocalInfo();
        fo.setInstanceId(instanceLocalInfo.getInstanceId());
        fo.setServiceId(instanceLocalInfo.getServiceId());
        return grayEventRemoteClient.listeningNewestStatus(fo);
    }


    private GrayEventRetrieveResult retrieveNewestEvents() {
        RetrieveFO fo = new RetrieveFO();
        fo.setSortMark(grayEventReceiver.getLocationNewestSortMark());
        InstanceLocalInfo instanceLocalInfo = getInstanceLocalInfo();
        fo.setInstanceId(instanceLocalInfo.getInstanceId());
        fo.setServiceId(instanceLocalInfo.getServiceId());
        return grayEventRemoteClient.retrieveNewestEvents(fo);
    }


    private InstanceLocalInfo getInstanceLocalInfo() {
        return instanceLocalInfoObtainer.getInstanceLocalInfo();
    }

    private boolean isStop() {
        return stop;
    }

    public void shutdown() {
        executor.shutdown();
        executorService.shutdown();
    }

}
