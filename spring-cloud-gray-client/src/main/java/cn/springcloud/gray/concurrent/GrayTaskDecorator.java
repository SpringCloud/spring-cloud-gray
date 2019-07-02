package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.core.task.TaskDecorator;

public class GrayTaskDecorator implements TaskDecorator {

    private RequestLocalStorage requestLocalStorage;

    public GrayTaskDecorator(RequestLocalStorage requestLocalStorage) {
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        GrayTrackInfo grayTrackInfo = requestLocalStorage.getGrayTrackInfo();
        if (grayTrackInfo == null) {
            return runnable;
        }

        GrayRunnableContext context = new GrayRunnableContext();
        context.setTarget(runnable);
        context.setGrayTrackInfo(grayTrackInfo);
        context.setRequestLocalStorage(requestLocalStorage);
        return new GrayRunnable(context);
    }
}
