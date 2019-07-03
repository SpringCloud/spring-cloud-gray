package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.core.task.TaskDecorator;

public class GrayTaskDecorator implements TaskDecorator {


    public GrayTaskDecorator(RequestLocalStorage requestLocalStorage) {
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return GrayConcurrentHelper.createDelegateRunnable(runnable);
    }
}
