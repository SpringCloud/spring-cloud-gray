package cn.springcloud.gray.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 */
public interface StreamInput {

    String INPUT = "GrayEventInput";

    @Input(INPUT)
    SubscribableChannel input();

}
