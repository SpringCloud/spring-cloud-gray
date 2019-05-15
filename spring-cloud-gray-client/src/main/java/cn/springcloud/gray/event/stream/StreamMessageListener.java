package cn.springcloud.gray.event.stream;

import cn.springcloud.gray.event.GrayEventListener;
import cn.springcloud.gray.event.GrayEventMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.io.IOException;


public class StreamMessageListener {

    private static final Logger log = LoggerFactory.getLogger(StreamMessageListener.class);


    private GrayEventListener grayEventListener;

    public StreamMessageListener(GrayEventListener grayEventListener) {
        this.grayEventListener = grayEventListener;
    }

    @StreamListener(StreamInput.INPUT)
    public void receive(GrayEventMsg msg) throws IOException {
        log.debug("接收到消息:{}", msg);
        grayEventListener.onEvent(msg);
    }

}
