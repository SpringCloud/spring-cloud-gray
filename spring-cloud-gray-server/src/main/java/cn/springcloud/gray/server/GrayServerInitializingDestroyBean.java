package cn.springcloud.gray.server;

import cn.springcloud.gray.concurrent.DefaultThreadFactory;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.manager.GrayServiceManager;
import cn.springcloud.gray.server.module.GrayInstanceRecordEvictor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GrayServerInitializingDestroyBean
        implements InitializingBean, ApplicationContextAware {
    private GrayServiceManager grayServiceManager;
    private GrayServerProperties grayServerProperties;
    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("initDestory"));
    private ApplicationContext appCxt;

    public GrayServerInitializingDestroyBean(
            GrayServiceManager grayServiceManager, GrayServerProperties grayServerProperties) {
        this.grayServiceManager = grayServiceManager;
        this.grayServerProperties = grayServerProperties;
    }

    @Override
    public void afterPropertiesSet() {
        initToWork();
    }

    private void initToWork() {
        grayServiceManager.openForWork();

        // 初始化灰度实例的回收任务
        initGrayInstanceRecordEvictionTask();
    }

    @PreDestroy
    public void shutdown() {
        grayServiceManager.shutdown();
        scheduledExecutorService.shutdown();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCxt = applicationContext;
    }

    private void initGrayInstanceRecordEvictionTask() {
        GrayServerProperties.InstanceRecordEvictProperties evictProperties =
                grayServerProperties.getInstance().getEviction();
        if (!evictProperties.isEnabled()) {
            return;
        }
        String beanName = "grayInstanceRecordEvictor";
        GrayInstanceRecordEvictor instanceRecordEvictor =
                getBean(beanName, GrayInstanceRecordEvictor.class);
        if (instanceRecordEvictor == null) {
            log.error("没有找到名为{}的GrayInstanceRecordEvictor类型或者为GrayInstanceRecordEvictor类型的实例", beanName);
            throw new NullPointerException("没有找到GrayInstanceRecordEvictor类型的实例");
        }
        scheduledExecutorService.schedule(
                () -> instanceRecordEvictor.evict(),
                evictProperties.getEvictionIntervalTimerInMs(),
                TimeUnit.MILLISECONDS);
    }

    private <T> T getBean(String beanName, Class<T> cls) {
        T t = appCxt.getBean(beanName, cls);
        if (t == null) {
            t = appCxt.getBean(cls);
        }
        return t;
    }
}
