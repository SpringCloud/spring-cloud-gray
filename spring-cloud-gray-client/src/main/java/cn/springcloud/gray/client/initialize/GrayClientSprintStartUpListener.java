package cn.springcloud.gray.client.initialize;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-04 23:23
 */
public class GrayClientSprintStartUpListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //只有root application context 没有父容器
        if (Objects.nonNull(event.getApplicationContext().getParent())) {
            return;
        }

        //todo
    }
}
