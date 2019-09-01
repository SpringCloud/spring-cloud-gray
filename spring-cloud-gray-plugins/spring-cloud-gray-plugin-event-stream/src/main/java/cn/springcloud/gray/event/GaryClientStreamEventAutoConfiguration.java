package cn.springcloud.gray.event;

import cn.springcloud.gray.client.config.GrayEventAutoConfiguration;
import cn.springcloud.gray.event.stream.StreamInput;
import cn.springcloud.gray.event.stream.StreamMessageListener;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableBinding.class)
@EnableBinding({StreamInput.class})
@ConditionalOnProperty(value = "spring.cloud.stream.bindings.GrayEventInput.destination")
@AutoConfigureAfter(GrayEventAutoConfiguration.class)
public class GaryClientStreamEventAutoConfiguration {


    @Bean
    public StreamMessageListener streamMessageListener(GrayEventListener grayEventListener) {
        return new StreamMessageListener(grayEventListener);
    }

}
