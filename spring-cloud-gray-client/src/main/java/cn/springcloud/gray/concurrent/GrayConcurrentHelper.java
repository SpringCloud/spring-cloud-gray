package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class GrayConcurrentHelper {

    private GrayConcurrentHelper() {
    }


    /**
     * callable集合转装饰后的callable集合
     *
     * @param tasks callable集合
     * @param <V>   执行结果类型
     * @return 装饰后的callable集合
     */
    public static <V> Collection<Callable<V>> mapDelegateCallables(Collection<? extends Callable<V>> tasks) {
        return tasks.stream().map(GrayConcurrentHelper::createDelegateCallable).collect(Collectors.toList());
    }

    public static <V> Callable<V> createDelegateCallable(Callable<V> callable) {
        if (getGrayTrackInfo() != null) {
            return new GrayCallable<>(createGrayCallableContext(callable));
        }
        return callable;
    }

    public static Runnable createDelegateRunnable(Runnable runnable) {
        if (getGrayTrackInfo() != null) {
            return new GrayRunnable(createGrayRunnableContext(runnable));
        }
        return runnable;
    }

    public static GrayRunnableContext createGrayRunnableContext(Runnable runnable) {
        GrayRunnableContext context = new GrayRunnableContext();
        context.setLocalStorageLifeCycle(GrayClientHolder.getLocalStorageLifeCycle());
        context.setRequestLocalStorage(GrayClientHolder.getRequestLocalStorage());
        context.setGrayTrackInfo(getGrayTrackInfo());
        context.setTarget(runnable);
        return context;
    }

    public static <V> GrayCallableContext createGrayCallableContext(Callable<V> callable) {
        GrayCallableContext context = new GrayCallableContext();
        context.setRequestLocalStorage(GrayClientHolder.getRequestLocalStorage());
        context.setLocalStorageLifeCycle(GrayClientHolder.getLocalStorageLifeCycle());
        context.setGrayTrackInfo(getGrayTrackInfo());
        context.setTarget(callable);
        return context;
    }

    public static GrayTrackInfo getGrayTrackInfo() {
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        return requestLocalStorage == null ? null : requestLocalStorage.getGrayTrackInfo();
    }
}
