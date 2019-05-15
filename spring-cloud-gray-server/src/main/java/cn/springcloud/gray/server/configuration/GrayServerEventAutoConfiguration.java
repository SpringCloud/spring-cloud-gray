package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.server.event.stream.StreamGrayEventPublisher;
import cn.springcloud.gray.server.event.stream.StreamOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayServerEventAutoConfiguration {


    @Configuration
    @ConditionalOnClass(EnableBinding.class)
    @ConditionalOnProperty(value = "spring.cloud.stream.bindings.GrayEventOutput.destination")
    @EnableBinding({StreamOutput.class})
    public static class StreamEventConfiguration {

        @Bean
        public GrayEventPublisher grayEventPublisher(StreamOutput streamOutput) {
            return new StreamGrayEventPublisher(streamOutput);
        }
    }


}
