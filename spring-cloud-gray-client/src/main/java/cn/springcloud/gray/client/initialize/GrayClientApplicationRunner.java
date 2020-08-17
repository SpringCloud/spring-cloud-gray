package cn.springcloud.gray.client.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author saleson
 * @date 2020-08-16 17:02
 */
@Slf4j
public class GrayClientApplicationRunner implements ApplicationRunner {
    private GrayInfosInitializer grayInfosInitializer;

    public GrayClientApplicationRunner(GrayInfosInitializer grayInfosInitializer) {
        this.grayInfosInitializer = grayInfosInitializer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始装载灰度...");
        try {
            grayInfosInitializer.setup();
        } catch (BeansException e) {
            log.warn("灰度装载失败,cause:{}", e.getMessage());
        }
        log.info("灰度装载完成.");
    }
}
