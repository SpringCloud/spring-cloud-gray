package cn.springcloud.gray.client.netflix.hystrix;

import cn.springcloud.gray.request.LocalStorageLifeCycle;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.apache.commons.lang3.StringUtils;

public class HystrixLocalStorageCycle implements LocalStorageLifeCycle {


    private ThreadLocal<String> hystrixRequestContextInitializedMark = new ThreadLocal<>();


    @Override
    public void initContext() {
        initContext("");
    }

    @Override
    public void initContext(String mark) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
            hystrixRequestContextInitializedMark.set(mark);
        }
    }

    @Override
    public void closeContext() {
        String hystrixReqCxtInitedMark = hystrixRequestContextInitializedMark.get();
        if (hystrixReqCxtInitedMark != null) {
            hystrixRequestContextInitializedMark.remove();
            if (HystrixRequestContext.isCurrentThreadInitialized()) {
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }

        }
    }

    @Override
    public void closeContext(String mark) {
        String hystrixReqCxtInitedMark = hystrixRequestContextInitializedMark.get();
        if (hystrixReqCxtInitedMark != null && StringUtils.equals(mark, hystrixReqCxtInitedMark)) {
            hystrixRequestContextInitializedMark.remove();
            if (HystrixRequestContext.isCurrentThreadInitialized()) {
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }

        }
    }
}
