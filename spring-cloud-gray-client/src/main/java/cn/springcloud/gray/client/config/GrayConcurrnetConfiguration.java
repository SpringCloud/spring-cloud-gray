package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.concurrent.aspect.ExecutorGrayAspect;
import cn.springcloud.gray.concurrent.aspect.ExecutorServiceGrayAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(GrayManager.class)
@ConditionalOnProperty(value = "gray.client.threadpool.transparent-local-store.enabled", matchIfMissing = true)
public class GrayConcurrnetConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExecutorServiceGrayAspect executorServiceGrayAspect() {
        return new ExecutorServiceGrayAspect();
    }


    @Bean
    @ConditionalOnMissingBean
    public ExecutorGrayAspect executorGrayAspect() {
        return new ExecutorGrayAspect();
    }

}
