package cn.springcloud.gray.stream.component;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableBinding({TestSourceInput.class})
public class MessageAckListener {

    private static final Logger log = LoggerFactory.getLogger(MessageAckListener.class);


    @StreamListener(TestSourceInput.INPUT)
    public void receive(Object msg, @Header(AmqpHeaders.CHANNEL) Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        log.info("Received3:" + msg);
        boolean request = false;//true=重新发送
//        channel.basicReject(deliveryTag, request);
        channel.basicAck(deliveryTag, false);
    }

}
