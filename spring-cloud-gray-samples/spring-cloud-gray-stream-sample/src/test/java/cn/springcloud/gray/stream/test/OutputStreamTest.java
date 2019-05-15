package cn.springcloud.gray.stream.test;

import cn.springcloud.gray.stream.GrayStreamApplication;
import cn.springcloud.gray.stream.component.MessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayStreamApplication.class)
public class OutputStreamTest {

    @Autowired
    private MessageSender sender;


    @Test
    public void testOutput() {
        boolean bool = sender.send("hello");
        System.out.println(bool);
    }

}
