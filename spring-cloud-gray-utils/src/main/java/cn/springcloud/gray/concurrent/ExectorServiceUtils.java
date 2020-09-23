package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.utils.StringUtils;

import java.util.concurrent.*;

/**
 * @author saleson
 * @date 2020-09-23 23:43
 */
public class ExectorServiceUtils {

    public static ExecutorService createExecutorService(ConcurrnetProperties concurrnetProperties) {
        BlockingQueue<Runnable> workQueue = null;
        if (concurrnetProperties.getQueueSize() == 0) {
            workQueue = new SynchronousQueue<>();
        } else if (concurrnetProperties.getQueueSize() < 0) {
            workQueue = new LinkedBlockingQueue<>();
        } else {
            workQueue = new ArrayBlockingQueue<>(concurrnetProperties.getQueueSize());
        }

        ThreadFactory threadFactory = StringUtils.isEmpty(concurrnetProperties.getThreadPrefix()) ?
                Executors.defaultThreadFactory() : new DefaultThreadFactory(concurrnetProperties.getThreadPrefix());

        return new ThreadPoolExecutor(
                concurrnetProperties.getCorePoolSize(),
                concurrnetProperties.getMaximumPoolSize(),
                concurrnetProperties.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                workQueue,
                threadFactory
        );
    }
}
