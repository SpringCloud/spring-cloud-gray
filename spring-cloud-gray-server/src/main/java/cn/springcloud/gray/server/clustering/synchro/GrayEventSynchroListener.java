package cn.springcloud.gray.server.clustering.synchro;

import cn.springcloud.gray.utils.JsonUtils;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.server.GrayEventSender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-08-16 05:08
 */
@Slf4j
public class GrayEventSynchroListener implements SynchDataListener {


    private GrayEventSender sender;

    public GrayEventSynchroListener(GrayEventSender sender) {
        this.sender = sender;
    }

    @Override
    public String supportListenDatatype() {
        return SynchroDataTypeConstants.GRAY_EVENT;
    }

    @Override
    public void listen(SynchData synchData) {
        GrayEvent grayEvent = (GrayEvent) synchData.getData();
        log.info("接收到同步的GrayEvent数据, Synch Id:{}, ", synchData.getId(), JsonUtils.toJsonString(grayEvent));
        sender.send(grayEvent);
    }
}
