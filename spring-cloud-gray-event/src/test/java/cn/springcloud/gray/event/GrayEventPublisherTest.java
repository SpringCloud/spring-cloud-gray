package cn.springcloud.gray.event;

import cn.springcloud.gray.event.client.DefaultGrayDeventPublisher;
import cn.springcloud.gray.event.client.GrayEventListener;
import cn.springcloud.gray.event.client.GrayEventPublisher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-01-30 21:44
 */
public class GrayEventPublisherTest {


    public static List<Object> testList = new ArrayList<>();

    private GrayEventPublisher grayEventPublisher;


    @Before
    public void before() {
        List<GrayEventListener> grayEventListeners = new ArrayList<>();
        grayEventListeners.add(new GrayInstanceEventListener());
        grayEventListeners.add(new GrayPolicyEventListener());
        grayEventListeners.add(new GrayInstanceEventListener2());

        grayEventPublisher = new DefaultGrayDeventPublisher(grayEventListeners);
    }

    @Test
    public void test() {
        TGrayInstanceEvent grayInstanceEvent = new TGrayInstanceEvent(0, "Null");
        grayEventPublisher.publishEvent(grayInstanceEvent);
        Assert.assertTrue(testList.contains(grayInstanceEvent));

        System.out.println(testList);

        TGrayPolicyEvent grayPolicyEvent = new TGrayPolicyEvent(1, "A");
        grayEventPublisher.publishEvent(grayPolicyEvent);
        Assert.assertTrue(testList.contains(grayPolicyEvent));

        System.out.println(testList);
    }


}
