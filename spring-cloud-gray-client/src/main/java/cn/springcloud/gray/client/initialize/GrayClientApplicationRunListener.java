package cn.springcloud.gray.client.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-05 23:30
 */
@Slf4j
public class GrayClientApplicationRunListener implements SpringApplicationRunListener {


    public GrayClientApplicationRunListener(SpringApplication springApplication, String[] args) {

    }


    @Override
    public void starting() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
        try {
            GrayInfosInitializer grayInfosInitializer =
                    context.getBean("grayInfosInitializer", GrayInfosInitializer.class);
            if (Objects.isNull(grayInfosInitializer)) {
                return;
            }
            grayInfosInitializer.setup();
        } catch (BeansException e) {
            log.warn("springboot 启动之后初始化灰度信息失败,cause:{}", e.getMessage());
        }
    }
}
