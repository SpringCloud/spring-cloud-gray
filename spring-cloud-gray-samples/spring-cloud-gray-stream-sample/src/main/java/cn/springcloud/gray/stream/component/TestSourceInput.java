package cn.springcloud.gray.stream.component;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 */
public interface TestSourceInput {

    String INPUT = "TestInput";

    @Input(INPUT)
    SubscribableChannel input();

}
