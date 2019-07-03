package cn.springcloud.gray.concurrent;

import org.springframework.core.task.TaskDecorator;

public class GrayTaskDecorator implements TaskDecorator {


    public GrayTaskDecorator() {
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return GrayConcurrentHelper.createDelegateRunnable(runnable);
    }
}
