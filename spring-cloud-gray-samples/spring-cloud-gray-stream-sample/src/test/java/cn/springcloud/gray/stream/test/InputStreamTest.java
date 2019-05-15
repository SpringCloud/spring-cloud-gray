package cn.springcloud.gray.stream.test;


import cn.springcloud.gray.stream.GrayStreamApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayStreamApplication.class)
public class InputStreamTest {


    @Test
    public void testInput() throws InterruptedException {
        System.out.println("start testInput.....");
        Thread.sleep(100000l);
        System.out.println("end testInput.....");
    }
}
