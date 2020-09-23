package cn.springcloud.gray.event;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.event.stream.StreamInput;
import cn.springcloud.gray.event.stream.StreamMessageListener;
import cn.springcloud.gray.event.client.GrayEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableBinding.class)
@EnableBinding({StreamInput.class})
@ConditionalOnBean(GrayManager.class)
public class GaryClientStreamEventAutoConfiguration {


    @Bean
    @ConditionalOnProperty(value = "spring.cloud.stream.bindings.GrayEventInput.destination")
    public StreamMessageListener streamMessageListener(GrayEventPublisher grayEventPublisher) {
        return new StreamMessageListener(grayEventPublisher);
    }

}
