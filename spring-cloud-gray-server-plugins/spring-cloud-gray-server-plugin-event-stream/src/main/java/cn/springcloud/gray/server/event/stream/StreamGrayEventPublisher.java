package cn.springcloud.gray.server.event.stream;


import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.exceptions.EventException;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class StreamGrayEventPublisher implements GrayEventPublisher {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private StreamOutput sender;

    public StreamGrayEventPublisher(StreamOutput sender) {
        this.sender = sender;
    }

    public boolean send(Object obj) {
        return sender.output().send(MessageBuilder.withPayload(obj).build());
    }

    @Override
    public void publishEvent(GrayEventMsg msg) throws EventException {
        executorService.schedule(() -> send(msg), 100, TimeUnit.MILLISECONDS);
    }
}
