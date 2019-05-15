package cn.springcloud.gray.server.event.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StreamOutput {


    String OUTPUT = "GrayEventOutput";


    @Output(OUTPUT)
    MessageChannel output();
}
