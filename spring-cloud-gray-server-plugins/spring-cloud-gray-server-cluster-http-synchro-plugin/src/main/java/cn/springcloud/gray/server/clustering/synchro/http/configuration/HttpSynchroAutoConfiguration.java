package cn.springcloud.gray.server.clustering.synchro.http.configuration;

import cn.springcloud.gray.server.clustering.ServerCluster;
import cn.springcloud.gray.server.clustering.synchro.ServerSynchronizer;
import cn.springcloud.gray.server.clustering.synchro.http.HttpServerSynchronizer;
import cn.springcloud.gray.server.clustering.synchro.http.ServerSynchDataAcceptEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * @author saleson
 * @date 2020-08-16 04:48
 */
@Configuration
public class HttpSynchroAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServerSynchronizer serverSynchronizer(
            @Qualifier("synchroExecutorService") ExecutorService synchroExecutorService,
            ServerCluster serverCluster) {
        return new HttpServerSynchronizer(serverCluster, synchroExecutorService);
    }

    @Bean
    public ServerSynchDataAcceptEndpoint serverSynchDataAcceptEndpoint() {
        return new ServerSynchDataAcceptEndpoint();
    }
}
