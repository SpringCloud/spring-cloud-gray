package cn.springcloud.gray.concurrent;

import java.util.concurrent.Executor;

public class GrayExecutor implements Executor {

    private Executor delegater;

    public GrayExecutor(Executor delegater) {
        this.delegater = delegater;
    }

    @Override
    public void execute(Runnable command) {
        if (GrayConcurrentHelper.getGraTrackInfo() == null) {
            delegater.execute(command);
        }
        delegater.execute(GrayConcurrentHelper.createDelegateRunnable(command));
    }
}
