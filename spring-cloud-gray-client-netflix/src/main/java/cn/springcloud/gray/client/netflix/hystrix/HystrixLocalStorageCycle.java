package cn.springcloud.gray.client.netflix.hystrix;

import cn.springcloud.gray.request.LocalStorageLifeCycle;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class HystrixLocalStorageCycle implements LocalStorageLifeCycle {


    private ThreadLocal<Boolean> hystrixRequestContextInitialized = new ThreadLocal<>();


    @Override
    public void initContext() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
            hystrixRequestContextInitialized.set(true);
        }
    }

    @Override
    public void closeContext() {
        Boolean hystrixReqCxtInited = hystrixRequestContextInitialized.get();
        if (hystrixReqCxtInited != null && hystrixReqCxtInited && HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }
}
