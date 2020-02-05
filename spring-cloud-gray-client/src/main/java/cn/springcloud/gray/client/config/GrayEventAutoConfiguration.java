package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.UpdateableGrayManager;
import cn.springcloud.gray.event.GrayEventListener;
import cn.springcloud.gray.event.GraySourceEventListener;
import cn.springcloud.gray.event.sourcehander.*;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
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
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            CommunicableGrayTrackHolder grayTrackHolder) {
        return new GrayTrackEventHandler(instanceLocalInfoObtainer, grayTrackHolder);
    }

    @Bean
    public GrayInstanceEventHandler grayInstanceEventHandler(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            GrayManager grayManager) {
        return new GrayInstanceEventHandler(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayDecisionEventHandler grayDecisionEventHandler(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            UpdateableGrayManager grayManager) {
        return new GrayDecisionEventHandler(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayServiceEventHandler grayServiceEventHandler(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            UpdateableGrayManager grayManager) {
        return new GrayServiceEventHandler(grayManager, instanceLocalInfoObtainer);
    }

    @Bean
    public GrayPolicyEventHandler grayPolicyEventHandler(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer,
            UpdateableGrayManager grayManager) {
        return new GrayPolicyEventHandler(grayManager, instanceLocalInfoObtainer);
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
