package cn.springcloud.gray.client.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;

/**
 * @author saleson
 * @date 2020-08-16 17:02
 */
@Slf4j
@Order(value = -1)
public class GrayClientApplicationRunner implements ApplicationRunner {
    private GrayInfosInitializer grayInfosInitializer;
    private ApplicationEventPublisher applicationEventPublisher;

    public GrayClientApplicationRunner(
            GrayInfosInitializer grayInfosInitializer,
            ApplicationEventPublisher applicationEventPublisher) {
        this.grayInfosInitializer = grayInfosInitializer;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始装载灰度...");
        initializeGrayInfos();
        log.info("灰度装载完成.");
        publishGrayInitializedEvent();
    }

    public boolean initializeGrayInfos() {
        log.info("开始加载灰度信息...");
        try {
            grayInfosInitializer.setup();
        } catch (BeansException e) {
            log.warn("灰度信息加载失败,cause:{}", e.getMessage());
            return false;
        }
        log.info("灰度信息加载完成.");
        return true;
    }

    public void publishGrayInitializedEvent() {
        log.info("发布灰度初始化完成事件");
        applicationEventPublisher.publishEvent(new GrayInitializedEvent());
    }

}
