package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;

public class GrayRunnable implements Runnable {

    private GrayRunnableContext context;

    public GrayRunnable(GrayRunnableContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        GrayTrackInfo grayTrackInfo = context.getGrayTrackInfo();
        RequestLocalStorage requestLocalStorage = context.getRequestLocalStorage();
        requestLocalStorage.initContext();
        requestLocalStorage.setGrayTrackInfo(grayTrackInfo);
        try {
            context.getTarget().run();
        } finally {
            requestLocalStorage.closeContext();
        }
    }
}
