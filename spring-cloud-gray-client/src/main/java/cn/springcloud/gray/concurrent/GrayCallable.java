package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;

import java.util.concurrent.Callable;

public class GrayCallable<V> implements Callable<V> {

    private GrayCallableContext context;

    public GrayCallable(GrayCallableContext context) {
        this.context = context;
    }

    @Override
    public V call() throws Exception {
        GrayTrackInfo grayTrackInfo = context.getGrayTrackInfo();
        LocalStorageLifeCycle localStorageLifeCycle = context.getLocalStorageLifeCycle();
        localStorageLifeCycle.initContext();
        RequestLocalStorage requestLocalStorage = context.getRequestLocalStorage();
        requestLocalStorage.setGrayTrackInfo(grayTrackInfo);
        try {
            return (V) context.getTarget().call();
        } finally {
            localStorageLifeCycle.closeContext();
        }
    }
}
