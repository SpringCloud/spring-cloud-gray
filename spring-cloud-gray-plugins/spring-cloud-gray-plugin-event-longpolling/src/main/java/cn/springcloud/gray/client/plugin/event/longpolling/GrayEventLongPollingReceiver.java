package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties.LongPollingProperties;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.event.client.BasicGrayEventReceiver;
import cn.springcloud.gray.event.client.GrayEventPublisher;

/**
 * @author saleson
 * @date 2020-02-04 00:40
 */
public class GrayEventLongPollingReceiver extends BasicGrayEventReceiver {

    private volatile long currentSortMark = -1;
    private GrayEventPublisher grayEventPublisher;
    private GrayEventRemoteClient grayEventRemoteClient;
    private LongPollingProperties longPollingProperties;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;
    private LongPollingWorker longPollingWorker;


    public GrayEventLongPollingReceiver(
            LongPollingProperties longPollingProperties,
            GrayEventPublisher grayEventPublisher,
            GrayEventRemoteClient grayEventRemoteClient,
            InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.grayEventPublisher = grayEventPublisher;
        this.grayEventRemoteClient = grayEventRemoteClient;
        this.longPollingProperties = longPollingProperties;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;

    }

    @Override
    public void start() {
        longPollingWorker = new LongPollingWorker(
                longPollingProperties, grayEventRemoteClient, this, instanceLocalInfoObtainer);
    }


    @Override
    public void shutdown() {
        longPollingWorker.shutdown();
    }


    @Override

    public GrayEventPublisher getGrayEventPublisher() {
        return grayEventPublisher;
    }

    @Override
    public long getLocationNewestSortMark() {
        return currentSortMark;
    }


    @Override
    protected synchronized void updateLocationNewestSortMark(long sortMark) {
        if (sortMark > currentSortMark) {
            currentSortMark = sortMark;
        }
    }
}
