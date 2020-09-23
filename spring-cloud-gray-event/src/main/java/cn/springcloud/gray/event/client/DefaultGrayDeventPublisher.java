package cn.springcloud.gray.event.client;

import cn.springcloud.gray.event.GrayEvent;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author saleson
 * @date 2020-01-30 21:44
 */
public class DefaultGrayDeventPublisher extends AbstractGrayEventPublisher {

    private ExecutorService executorService;


    public DefaultGrayDeventPublisher(List<GrayEventListener> grayEventListeners) {
        super(grayEventListeners);
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    protected void invokeListenOnEvent(GrayEventListener listener, GrayEvent grayEvent) {
        if (executorService != null) {
            executorService.execute(() -> super.invokeListenOnEvent(listener, grayEvent));
        } else {
            super.invokeListenOnEvent(listener, grayEvent);
        }
    }
}
