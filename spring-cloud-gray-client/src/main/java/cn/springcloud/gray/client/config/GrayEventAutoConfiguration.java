package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.GrayEventListener;
import cn.springcloud.gray.event.GraySourceEventListener;
import cn.springcloud.gray.event.sourcehander.*;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springcloud.gray.request.track.CommunicableGrayTrackHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnBean(GrayManager.class)
public class GrayEventAutoConfiguration {


//    @Bean
//    @ConditionalOnMissingBean
//    public GrayEventListener grayEventListener(
//            @Autowired(required = false) CommunicableGrayTrackHolder grayTrackHolder,
//            CommunicableGrayManager grayManager) {
//        return new DefaultGrayEventListener(grayTrackHolder, grayManager);
//    }


    @Bean
    public GrayTrackEventHandler grayTrackEventHandler(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            CommunicableGrayTrackHolder grayTrackHolder) {
        return new GrayTrackEventHandler(instanceLocalInfoInitiralizer, grayTrackHolder);
    }

    @Bean
    public GrayInstanceEventHandler grayInstanceEventHandler(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            GrayManager grayManager) {
        return new GrayInstanceEventHandler(grayManager, instanceLocalInfoInitiralizer);
    }

    @Bean
    public GrayDecisionEventHandler grayDecisionEventHandler(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            UpdateableGrayManager grayManager) {
        return new GrayDecisionEventHandler(grayManager, instanceLocalInfoInitiralizer);
    }

    @Bean
    public GrayServiceEventHandler grayServiceEventHandler(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            UpdateableGrayManager grayManager) {
        return new GrayServiceEventHandler(grayManager, instanceLocalInfoInitiralizer);
    }

    @Bean
    public GrayPolicyEventHandler grayPolicyEventHandler(
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer,
            UpdateableGrayManager grayManager) {
        return new GrayPolicyEventHandler(grayManager, instanceLocalInfoInitiralizer);
    }


    @Bean
    @ConditionalOnMissingBean
    public SourceHanderService sourceHanderService(List<GraySourceEventHandler> handlers) {
        return new SourceHanderService.Default(handlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayEventListener grayEventListener(SourceHanderService sourceHanderService) {
        return new GraySourceEventListener(sourceHanderService);
    }


}
