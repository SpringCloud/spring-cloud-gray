package cn.springcloud.gray.server.event.stream;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.server.GrayEventSender;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-05-05 12:22
 */
public class GrayEventStreamSender implements GrayEventSender {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private StreamOutput sender;

    public GrayEventStreamSender(StreamOutput sender) {
        this.sender = sender;
    }

    @Override
    public void send(GrayEvent grayEvent) {
        executorService.schedule(() -> sendMsg(grayEvent), 100, TimeUnit.MILLISECONDS);
    }

    public boolean sendMsg(Object obj) {
        return sender.output().send(MessageBuilder.withPayload(obj).build());
    }
}
