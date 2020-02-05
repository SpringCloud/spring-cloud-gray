package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.listener.*;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
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
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            CommunicableGrayTrackHolder grayTrackHolder) {
        return new GrayTrackEventListener(instanceLocalInfoObtainer, grayTrackHolder);
    }

    @Bean
    public GrayInstanceEventListener grayInstanceEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            GrayManager grayManager) {
        return new GrayInstanceEventListener(grayManager, instanceLocalInfoObtainer);
    }


    @Bean
    public GrayPolicyEventListener grayPolicyEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            UpdateableGrayManager grayManager) {
        return new GrayPolicyEventListener(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GraServiceEventListener graServiceEventListener(UpdateableGrayManager grayManager) {
        return new GraServiceEventListener(grayManager);
    }

    @Bean
    public GrayDecisionEventListener grayDecisionEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            UpdateableGrayManager grayManager) {
        return new GrayDecisionEventListener(grayManager, instanceLocalInfoObtainer);
    }


}
