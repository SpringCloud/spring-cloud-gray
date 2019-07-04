package cn.springcloud.gray.service.b.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ConcurrentConfiguration {

//    @Bean
//    public ExecutorServiceGrayAspect executorServiceGrayAspect() {
//        return new ExecutorServiceGrayAspect();
//    }


    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
    }
}
