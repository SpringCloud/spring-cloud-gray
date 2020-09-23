package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.concurrent.ExecutorConcurrentStrategy;
import cn.springcloud.gray.server.clustering.synchro.GrayEventSynchroListener;
import cn.springcloud.gray.server.clustering.synchro.SimpleSynchDataAcceptor;
import cn.springcloud.gray.server.clustering.synchro.SynchDataAcceptor;
import cn.springcloud.gray.server.clustering.synchro.SynchDataListener;
import cn.springcloud.gray.server.configuration.properties.ClusterProperties;
import cn.springcloud.gray.event.server.GrayEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-08-16 05:12
 */
@Configuration
@ConditionalOnProperty(value = "gray.server.cluster.synchro.enable", matchIfMissing = true)
public class SynchroAutoConfiguration {

    @Autowired
    private ClusterProperties clusterProperties;


    @Bean
    @ConditionalOnMissingBean(name = "synchroExecutorService")
    public ExecutorService synchroExecutorService() {
        ExecutorConcurrentStrategy ecs = clusterProperties.
                getSynchro().getExecutorConcurrentStrategy();
        return new ThreadPoolExecutor(ecs.getCorePoolSize(), ecs.getMaximumPoolSize(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(ecs.getQueueSize()));
    }


    @Bean
    @ConditionalOnMissingBean
    public SynchDataAcceptor synchDataAcceptor(
            @Qualifier("synchroExecutorService") ExecutorService synchroExecutorService,
            List<SynchDataListener> synchDataListeners) {
        return new SimpleSynchDataAcceptor(synchroExecutorService, synchDataListeners);
    }


    @Bean
    public GrayEventSynchroListener grayEventSynchroListener(GrayEventSender sender) {
        return new GrayEventSynchroListener(sender);
    }

}
