package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springlcoud.gray.event.client.GrayEventReceiver;

/**
 * @author saleson
 * @date 2020-02-04 00:40
 */
public class GrayEventLongPollingReceiver implements GrayEventReceiver {

    private GrayEventRemoteClient grayEventRemoteClient;
    private volatile long currentSortMark;

    @Override
    public void start(long sortMark) {
        this.currentSortMark = sortMark;

    }


    @Override
    public void shutdown() {

    }


}
