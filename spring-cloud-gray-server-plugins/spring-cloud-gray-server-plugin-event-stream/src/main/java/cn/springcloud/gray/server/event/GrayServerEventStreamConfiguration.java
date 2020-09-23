package cn.springcloud.gray.server.event;

import cn.springcloud.gray.server.event.stream.GrayEventStreamSender;
import cn.springcloud.gray.server.event.stream.StreamOutput;
import cn.springcloud.gray.event.server.GrayEventSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableBinding.class)
@ConditionalOnProperty(value = "spring.cloud.stream.bindings.GrayEventOutput.destination")
@EnableBinding({StreamOutput.class})
public class GrayServerEventStreamConfiguration {

    @Bean
    public GrayEventSender grayEventSender(StreamOutput streamOutput) {
        return new GrayEventStreamSender(streamOutput);
    }
}
