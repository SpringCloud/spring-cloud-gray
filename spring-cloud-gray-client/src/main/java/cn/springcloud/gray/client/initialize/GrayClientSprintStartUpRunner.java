package cn.springcloud.gray.client.initialize;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-04 23:29
 */
public class GrayClientSprintStartUpRunner implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        GrayInfosInitializer grayInfosInitializer =
                applicationContext.getBean("grayInfosInitializer", GrayInfosInitializer.class);
        if (!Objects.isNull(grayInfosInitializer)) {
            return;
        }
        grayInfosInitializer.setup();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
