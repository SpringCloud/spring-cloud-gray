package cn.springcloud.gray.server.clustering.synchro;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-08-16 05:05
 */
@Slf4j
public class SimpleSynchDataAcceptor implements SynchDataAcceptor {

    private List<SynchDataListener> synchDataListeners;
    private Map<String, List<SynchDataListener>> dataTypeListenerCache = new HashMap<>();
    private ExecutorService executorService;

    public SimpleSynchDataAcceptor(List<SynchDataListener> synchDataListeners) {
        this(new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10)), synchDataListeners);
    }

    public SimpleSynchDataAcceptor(ExecutorService executorService, List<SynchDataListener> synchDataListeners) {
        this.executorService = executorService;
        this.synchDataListeners = synchDataListeners;
        refreshDataTypeListenerCache();
    }

    @Override
    public void accept(SynchData synchData) {
        log.info("接收到同步数据 -> id:{}, dataType:{}, sendTime:{}",
                synchData.getId(), synchData.getDataType(), timeStampFormat(synchData.getTimestamp()));
        List<SynchDataListener> listeners = dataTypeListenerCache.get(synchData.getDataType());
        if (Objects.isNull(listeners)) {
            return;
        }
        executorService.execute(() -> {
            for (SynchDataListener synchDataListener : listeners) {
                synchDataListener.listen(synchData);
            }
        });
    }

    private void refreshDataTypeListenerCache() {
        Map<String, List<SynchDataListener>> cache = new HashMap<>();
        for (SynchDataListener synchDataListener : synchDataListeners) {
            List<SynchDataListener> listeners = cache.get(synchDataListener.supportListenDatatype());
            if (Objects.isNull(listeners)) {
                listeners = new ArrayList<>();
                cache.put(synchDataListener.supportListenDatatype(), listeners);
            }
            listeners.add(synchDataListener);
        }
        this.dataTypeListenerCache = cache;
    }

    private DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String timeStampFormat(long time) {
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }
}
