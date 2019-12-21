package cn.springcloud.gray.apollo.sample.core;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author saleson
 * @date 2019-12-21 21:34
 */
public class ApolloAutoRefreshListener implements ConfigChangeListener {
    private static final Logger log = LoggerFactory.getLogger(ApolloAutoRefreshListener.class);

    private EnvironmentRefresher environmentRefresher;

    private AtomicBoolean ready = new AtomicBoolean(false);

    private AtomicBoolean refreshed = new AtomicBoolean(false);

    public ApolloAutoRefreshListener(EnvironmentRefresher environmentRefresher) {
        this.environmentRefresher = environmentRefresher;
    }

    @EventListener
    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        if (!ready.get()) {
            //应用未启动完全
            return;
        }

        if (refreshed.get()) {
            //已有线程在刷新
            return;
        }
        refreshed.compareAndSet(false, true);
        log.debug("ApolloChangeEvent received [{}]", changeEvent.getNamespace());
        environmentRefresher.refresh(changeEvent);
        log.info("ApolloChangeKeys[{}]. ", changeEvent.changedKeys());
        refreshed.compareAndSet(true, false);

    }
}
