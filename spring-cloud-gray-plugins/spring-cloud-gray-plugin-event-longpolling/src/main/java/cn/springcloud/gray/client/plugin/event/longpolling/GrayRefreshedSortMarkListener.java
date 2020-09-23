package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springcloud.gray.model.GrayInfos;
import cn.springcloud.gray.refresh.GrayInformationRefresher;
import cn.springcloud.gray.refresh.GrayRefreshedEvent;
import cn.springcloud.gray.event.client.GrayEventReceiver;
import org.springframework.context.ApplicationListener;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-05 13:47
 */
public class GrayRefreshedSortMarkListener implements ApplicationListener<GrayRefreshedEvent> {

    private GrayEventReceiver grayEventReceiver;
    private volatile boolean longPollingEnabled = false;

    public GrayRefreshedSortMarkListener(GrayEventReceiver grayEventReceiver) {
        this.grayEventReceiver = grayEventReceiver;
    }

    @Override

    public void onApplicationEvent(GrayRefreshedEvent event) {
        if (!Objects.equals(event.getTriggerName(), GrayInformationRefresher.TRIGGET_NAME)) {
            return;
        }
        GrayInfos grayInfos = (GrayInfos) event.getSource();
        grayEventReceiver.setLocationNewestSortMark(grayInfos.getMaxSortMark());
        if (!longPollingEnabled) {
            enableLongPolling();
        }
    }

    private synchronized void enableLongPolling() {
        if (longPollingEnabled) {
            return;
        }
        grayEventReceiver.start();
        longPollingEnabled = true;
    }
}
