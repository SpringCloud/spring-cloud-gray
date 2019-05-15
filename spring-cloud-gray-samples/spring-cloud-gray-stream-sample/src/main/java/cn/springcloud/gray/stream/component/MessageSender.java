package cn.springcloud.gray.stream.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
@EnableBinding({TestSourceOutput.class})
public class MessageSender {

    @Autowired
    private TestSourceOutput sender;


    public boolean send(Object obj) {
        return sender.output().send(MessageBuilder.withPayload(obj).build());
    }

}
