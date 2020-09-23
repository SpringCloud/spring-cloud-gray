package cn.springcloud.gray.server.event.triggering;

import cn.springcloud.gray.server.clustering.synchro.ServerSynchronizer;
import cn.springcloud.gray.server.clustering.synchro.SynchData;
import cn.springcloud.gray.server.clustering.synchro.SynchroDataTypeConstants;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.server.GrayEventObserveState;
import cn.springcloud.gray.event.server.GrayEventObserver;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-08-16 04:32
 */
public class GrayEventServerSynchroObserver implements GrayEventObserver {

    private ServerSynchronizer serverSynchronizer;

    public GrayEventServerSynchroObserver(ServerSynchronizer serverSynchronizer) {
        this.serverSynchronizer = serverSynchronizer;
    }

    @Override
    public void observe(GrayEventObserveState observeState, GrayEvent grayEvent) {
        if (!Objects.equals(GrayEventObserveState.CREATED, observeState)) {
            return;
        }

        SynchData synchData = new SynchData();
        synchData.setDataType(SynchroDataTypeConstants.GRAY_EVENT);
        synchData.setData(grayEvent);
        serverSynchronizer.broadcast(synchData);
    }
}
