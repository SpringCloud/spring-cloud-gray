package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.clustering.synchro.GrayEventSynchroListener;
import cn.springcloud.gray.server.clustering.synchro.SimpleSynchDataAcceptor;
import cn.springcloud.gray.server.clustering.synchro.SynchDataAcceptor;
import cn.springcloud.gray.server.clustering.synchro.SynchDataListener;
import cn.springlcoud.gray.event.server.GrayEventSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author saleson
 * @date 2020-08-16 05:12
 */
@Configuration
public class SynchroAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public SynchDataAcceptor synchDataAcceptor(List<SynchDataListener> synchDataListeners) {
        return new SimpleSynchDataAcceptor(synchDataListeners);
    }


    @Bean
    public GrayEventSynchroListener grayEventSynchroListener(GrayEventSender sender) {
        return new GrayEventSynchroListener(sender);
    }

}
