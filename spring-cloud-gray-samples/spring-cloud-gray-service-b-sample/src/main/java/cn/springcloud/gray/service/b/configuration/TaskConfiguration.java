package cn.springcloud.gray.service.b.configuration;

import cn.springcloud.gray.concurrent.GrayTaskDecorator;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:异步任务线程池
 *
 * @author JianqiangZhao
 * @date 2019/1/14
 */
@Configuration
public class TaskConfiguration {
    @Bean("taskExecutor")
    public Executor taskExecutor(RequestLocalStorage requestLocalStorage) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(400);
        executor.setQueueCapacity(400);
        executor.setKeepAliveSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setTaskDecorator(new GrayTaskDecorator(requestLocalStorage));
        return executor;
    }
}
