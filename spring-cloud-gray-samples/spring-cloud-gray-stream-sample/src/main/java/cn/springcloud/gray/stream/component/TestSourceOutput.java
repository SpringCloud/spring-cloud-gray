package cn.springcloud.gray.stream.component;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


/**
 * topic see
 */
public interface TestSourceOutput {

    String OUTPUT = "TestOutput";


    @Output(OUTPUT)
    MessageChannel output();


}
