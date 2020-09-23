package cn.springcloud.gray.event.stream;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.client.GrayEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;


/**
 * spring cloud stream 订阅灰度事件的接收通道（channel），接收灰度事件，并交由GrayEventListener处理
 */
public class StreamMessageListener {

    private static final Logger log = LoggerFactory.getLogger(StreamMessageListener.class);


    private GrayEventPublisher grayEventPublisher;

    public StreamMessageListener(GrayEventPublisher grayEventPublisher) {
        this.grayEventPublisher = grayEventPublisher;
    }

    /**
     * 接收灰度事件
     *
     * @param event 灰度事件
     */
    @StreamListener(StreamInput.INPUT)
    public void receive(GrayEvent event) {
        log.debug("接收到消息:{}", event);
        grayEventPublisher.publishEvent(event);
    }

}
