package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.Pair;
import cn.springcloud.gray.mock.DefaultMockDriver;
import cn.springcloud.gray.mock.MockAction;
import cn.springcloud.gray.mock.MockDriver;
import cn.springcloud.gray.mock.MockHandle;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-05-17 23:13
 */
public class MockActionFactoryTest {

    @Test
    public void testPauseMockAction() {
        PauseMockActionFactory mockActionFactory = new PauseMockActionFactory();

        Map<String, String> info = new HashMap<>();
        info.put("time", "5000");
        MockAction mockAction = mockActionFactory.create(info);
        long startTime = System.currentTimeMillis();
        System.out.println("start at " + startTime);
        mockAction.mock(null);
        long endTime = System.currentTimeMillis();
        System.out.println("end at " + endTime + " used " + (endTime - startTime) + "ms");
    }


    @Test
    public void testMockDriver() {

        MockDriver mockDriver = new DefaultMockDriver();
        mockDriver.addMockActionFactory(new PauseMockActionFactory());
        mockDriver.addMockActionFactory(new HttpResponseMockActionFactory());

        Map<String, Object> info = new HashMap<>();
        info.put("time", "500");
//        Map<String, String> headers = new HashMap<>();
//        headers.put("length", "1000");
//        info.put("headers", headers);
        info.put("headers", "{\"length\":\"1000\"}");
        info.put("body", "{'code':'0', 'msg':'test!!!'}");

        MockHandle mockHandle = mockDriver.createMockHandle(new Pair<>("Pause", info), new Pair<>("HttpResponse", info));

        long startTime = System.currentTimeMillis();
        System.out.println("start at " + startTime);
        Object result = mockHandle.mock(null);
        long endTime = System.currentTimeMillis();
        System.out.println("end at " + endTime + " used " + (endTime - startTime) + "ms");
        System.out.println("result: " + result);
    }
}
