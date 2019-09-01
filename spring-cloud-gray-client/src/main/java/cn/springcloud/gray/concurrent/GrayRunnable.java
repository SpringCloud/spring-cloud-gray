package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;

public class GrayRunnable implements Runnable {

    private GrayRunnableContext context;

    public GrayRunnable(GrayRunnableContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        GrayConcurrentHelper.initRequestLocalStorageContext(context);
        try {
            context.getTarget().run();
        } finally {
            GrayConcurrentHelper.cleanRequestLocalStorageContext(context);
        }
    }
}
