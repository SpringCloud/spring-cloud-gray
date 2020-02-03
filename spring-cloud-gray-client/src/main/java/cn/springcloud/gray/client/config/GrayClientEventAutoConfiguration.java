package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.listener.GrayDecisionEventListener;
import cn.springcloud.gray.event.listener.GrayInstanceEventListener;
import cn.springcloud.gray.event.listener.GrayPolicyEventListener;
import cn.springcloud.gray.event.listener.GrayTrackEventListener;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springcloud.gray.request.track.CommunicableGrayTrackHolder;
import cn.springlcoud.gray.event.client.DefaultGrayDeventPublisher;
import cn.springlcoud.gray.event.client.GrayEventListener;
import cn.springlcoud.gray.event.client.GrayEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author saleson
 * @date 2020-02-03 19:59
 */
@Configuration
@ConditionalOnBean(GrayManager.class)
public class GrayClientEventAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public GrayEventPublisher grayEventPublisher(List<GrayEventListener> listeners) {
        return new DefaultGrayDeventPublisher(listeners);
    }

    @Bean
    public GrayTrackEventListener grayTrackEventListener(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            CommunicableGrayTrackHolder grayTrackHolder) {
        return new GrayTrackEventListener(instanceLocalInfoInitiralizer, grayTrackHolder);
    }

    @Bean
    public GrayInstanceEventListener grayInstanceEventListener(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            GrayManager grayManager) {
        return new GrayInstanceEventListener(grayManager, instanceLocalInfoInitiralizer);
    }


    @Bean
    public GrayPolicyEventListener grayPolicyEventListener(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            UpdateableGrayManager grayManager) {
        return new GrayPolicyEventListener(grayManager, instanceLocalInfoInitiralizer);
    }

    @Bean
    public GrayDecisionEventListener grayDecisionEventListener(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            UpdateableGrayManager grayManager) {
        return new GrayDecisionEventListener(grayManager, instanceLocalInfoInitiralizer);
    }


}
