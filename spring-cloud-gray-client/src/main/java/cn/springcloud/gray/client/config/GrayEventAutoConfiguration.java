package cn.springcloud.gray.client.config;

import cn.springcloud.gray.CommunicableGrayManager;
import cn.springcloud.gray.event.DefaultGrayEventListener;
import cn.springcloud.gray.event.GrayEventListener;
import cn.springcloud.gray.event.stream.StreamInput;
import cn.springcloud.gray.event.stream.StreamMessageListener;
import cn.springcloud.gray.request.track.CommunicableGrayTrackHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayEventAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public GrayEventListener grayEventListener(
            CommunicableGrayTrackHolder grayTrackHolder, CommunicableGrayManager grayManager) {
        return new DefaultGrayEventListener(grayTrackHolder, grayManager);
    }


    @Configuration
    @ConditionalOnClass(EnableBinding.class)
    @EnableBinding({StreamInput.class})
    @ConditionalOnProperty(value = "spring.cloud.stream.bindings.GrayEventInput.destination")
    public static class StreamEventConfiguration {

        @Bean
        public StreamMessageListener streamMessageListener(GrayEventListener grayEventListener) {
            return new StreamMessageListener(grayEventListener);
        }

    }

}
