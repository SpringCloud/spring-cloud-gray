package cn.springcloud.gray.stream.test;

import cn.springcloud.gray.stream.component.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GrayStreamApplication.class)
public class OutputStreamTest {

    @Autowired
    private MessageSender sender;


    //    @Test
    public void testOutput() {
        boolean bool = sender.send("hello");
        System.out.println(bool);
    }

}
