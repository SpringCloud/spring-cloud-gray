package cn.springcloud.gray.server.event.stream;


import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.exceptions.EventException;
import org.springframework.messaging.support.MessageBuilder;


public class StreamGrayEventPublisher implements GrayEventPublisher {

    private StreamOutput sender;

    public StreamGrayEventPublisher(StreamOutput sender) {
        this.sender = sender;
    }

    public boolean send(Object obj) {
        return sender.output().send(MessageBuilder.withPayload(obj).build());
    }

    @Override
    public void publishEvent(GrayEventMsg msg) throws EventException {
        send(msg);
    }
}
