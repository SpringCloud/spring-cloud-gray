package cn.springcloud.gray.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 */
public interface StreamInput {


    /**
     * 灰度事件接收的spring cloud stram 通道
     */
    String INPUT = "GrayEventInput";

    @Input(INPUT)
    SubscribableChannel input();

}
