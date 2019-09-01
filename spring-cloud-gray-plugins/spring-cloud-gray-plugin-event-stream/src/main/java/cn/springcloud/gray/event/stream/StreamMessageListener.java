package cn.springcloud.gray.event.stream;

import cn.springcloud.gray.event.GrayEventListener;
import cn.springcloud.gray.event.GrayEventMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;


/**
 * spring cloud stream 订阅灰度事件的接收通道（channel），接收灰度事件，并交由GrayEventListener处理
 */
public class StreamMessageListener {

    private static final Logger log = LoggerFactory.getLogger(StreamMessageListener.class);


    private GrayEventListener grayEventListener;

    public StreamMessageListener(GrayEventListener grayEventListener) {
        this.grayEventListener = grayEventListener;
    }


    /**
     * 接收灰度事件
     *
     * @param msg 灰度事件
     */
    @StreamListener(StreamInput.INPUT)
    public void receive(GrayEventMsg msg) {
        log.debug("接收到消息:{}", msg);
        grayEventListener.onEvent(msg);
    }

}
