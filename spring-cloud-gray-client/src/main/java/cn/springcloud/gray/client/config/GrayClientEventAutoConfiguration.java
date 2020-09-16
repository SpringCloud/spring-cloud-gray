package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.event.listener.*;
import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springlcoud.gray.event.client.DefaultGrayDeventPublisher;
import cn.springlcoud.gray.event.client.GrayEventListener;
import cn.springlcoud.gray.event.client.GrayEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;

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
        OrderComparator.sort(listeners);
        return new DefaultGrayDeventPublisher(listeners);
    }

    @Bean
    public GrayTrackEventListener grayTrackEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            GrayTrackHolder grayTrackHolder) {
        return new GrayTrackEventListener(instanceLocalInfoObtainer, grayTrackHolder);
    }

    @Bean
    public GrayInstanceEventListener grayInstanceEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            GrayManager grayManager) {
        return new GrayInstanceEventListener(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayInstanceAliasEventListener grayInstanceAliasEventListener(
            GrayManager grayManager) {
        return new GrayInstanceAliasEventListener(grayManager);
    }

    @Bean
    @ConditionalOnMissingBean(name = "routePolicyEventListener")
    public RoutePolicyEventListener routePolicyEventListener(
            GrayManager grayManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        return new RoutePolicyEventListener(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayPolicyEventListener grayPolicyEventListener(PolicyDecisionManager policyDecisionManager) {
        return new GrayPolicyEventListener(policyDecisionManager);
    }

    @Bean
    public GrayServiceEventListener grayServiceEventListener(UpdateableGrayManager grayManager) {
        return new GrayServiceEventListener(grayManager);
    }

    @Bean
    public GrayDecisionEventListener grayDecisionEventListener(
            PolicyDecisionManager policyDecisionManager) {
        return new GrayDecisionEventListener(policyDecisionManager);
    }

    @Bean
    public HandleActionEventlistener handleActionEventlistener(HandleManager handleManager) {
        return new HandleActionEventlistener(handleManager);
    }

    @Bean
    public HandleEventlistener handleEventlistener(HandleManager handleManager) {
        return new HandleEventlistener(handleManager);
    }


    @Bean
    public HandleRuleEventlistener handleRuleEventlistener(HandleRuleManager handleRuleManager) {
        return new HandleRuleEventlistener(handleRuleManager);
    }


}
