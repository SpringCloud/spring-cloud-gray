package cn.springcloud.gray.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static cn.springcloud.gray.concurrent.GrayConcurrentHelper.*;

public class GrayExecutorService implements ExecutorService {

    private ExecutorService delegater;

    public GrayExecutorService(ExecutorService delegater) {
        this.delegater = delegater;
    }

    @Override
    public void shutdown() {
        delegater.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegater.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegater.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegater.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegater.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegater.submit(createDelegateCallable(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return delegater.submit(createDelegateRunnable(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegater.submit(createDelegateRunnable(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (getGraTrackInfo() != null) {
            return delegater.invokeAll(mapDelegateCallables(tasks));
        }
        return delegater.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        if (getGraTrackInfo() != null) {
            return delegater.invokeAll(mapDelegateCallables(tasks), timeout, unit);
        }
        return delegater.invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        if (getGraTrackInfo() != null) {
            return delegater.invokeAny(mapDelegateCallables(tasks));
        }
        return delegater.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getGraTrackInfo() != null) {
            return delegater.invokeAny(mapDelegateCallables(tasks), timeout, unit);
        }
        return delegater.invokeAny(tasks);
    }

    @Override
    public void execute(Runnable command) {
        delegater.execute(createDelegateRunnable(command));
    }


}
