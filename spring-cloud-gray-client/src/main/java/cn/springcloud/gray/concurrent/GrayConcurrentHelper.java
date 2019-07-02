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
        GrayCallableContext context = createGrayCallableContext(callable);
        if (context.getGrayTrackInfo() != null) {
            return new GrayCallable<>(context);
        }
        return callable;
    }

    public static Runnable createDelegateRunnable(Runnable runnable) {
        GrayRunnableContext context = createGrayRunnableContext(runnable);
        if (context.getGrayTrackInfo() != null) {
            return new GrayRunnable(context);
        }
        return runnable;
    }

    public static GrayRunnableContext createGrayRunnableContext(Runnable runnable) {
        GrayRunnableContext context = new GrayRunnableContext();
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        context.setRequestLocalStorage(requestLocalStorage);
        context.setGrayTrackInfo(getGraTrackInfo());
        context.setTarget(runnable);
        return context;
    }

    public static <V> GrayCallableContext createGrayCallableContext(Callable<V> callable) {
        GrayCallableContext context = new GrayCallableContext();
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        context.setRequestLocalStorage(requestLocalStorage);
        context.setGrayTrackInfo(getGraTrackInfo());
        context.setTarget(callable);
        return context;
    }

    public static GrayTrackInfo getGraTrackInfo() {
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        return requestLocalStorage == null ? null : requestLocalStorage.getGrayTrackInfo();
    }
}
