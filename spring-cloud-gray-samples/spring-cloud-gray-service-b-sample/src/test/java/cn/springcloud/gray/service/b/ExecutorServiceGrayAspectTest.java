package cn.springcloud.gray.service.b;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceBApplication.class)
public class ExecutorServiceGrayAspectTest {

    @Autowired
    private LocalStorageLifeCycle localStorageLifeCycle;

    @Autowired
    private ExecutorService executorService;
    @Autowired
    private RequestLocalStorage requestLocalStorage;


    @Before
    public void before() {
        localStorageLifeCycle.initContext();
    }

    @After
    public void after() {
        localStorageLifeCycle.closeContext();
    }


    @Test
    public void testExecute() {
        requestLocalStorage.setGrayTrackInfo(new GrayHttpTrackInfo());
        executorService.execute(() -> {
            System.out.println("323fd");
        });
    }

    @Test
    public void testSubmit() {
        executorService.submit(() -> {
            System.out.println("323fd");
        });
    }

    @Test
    public void testSubmit2() {
        executorService.submit(() -> {
            System.out.println("323fd");
        }, "");
    }

    @Test
    public void testSubmit3() {
        executorService.submit(() -> {
            System.out.println("323fd");
            return "";
        });
    }


    @Test
    public void testInvokeAll() throws InterruptedException {
        executorService.invokeAll(Arrays.asList(() -> {
            System.out.println("323fd");
            return "";
        }, () -> {
            System.out.println("323fd");
            return "";
        }));
    }

    @Test
    public void testInvokeAll2() throws InterruptedException {
        executorService.invokeAll(Arrays.asList(() -> {
            System.out.println("323fd");
            return "";
        }, () -> {
            System.out.println("323fd");
            return "";
        }), 10, TimeUnit.SECONDS);
    }


    @Test
    public void testInvokeAny() throws InterruptedException, ExecutionException {
        executorService.invokeAny(Arrays.asList(() -> {
            System.out.println("323fd");
            return "";
        }, () -> {
            System.out.println("323fd");
            return "";
        }));
    }

    @Test
    public void testInvokeAny2() throws InterruptedException, ExecutionException, TimeoutException {
        executorService.invokeAny(Arrays.asList(() -> {
            System.out.println("323fd");
            return "";
        }, () -> {
            System.out.println("323fd");
            return "";
        }), 10, TimeUnit.SECONDS);
    }

}
