package cn.springcloud.gray.event.sourcehander;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.event.EventType;
import cn.springcloud.gray.event.GrayEventMsg;
import cn.springcloud.gray.event.SourceType;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springcloud.gray.model.GrayInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GrayInstanceEventHandler implements GraySourceEventHandler {

    private static final Logger log = LoggerFactory.getLogger(GrayInstanceEventHandler.class);

    private GrayManager grayManager;
    private InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer;

    public GrayInstanceEventHandler(GrayManager grayManager, InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoInitiralizer = instanceLocalInfoInitiralizer;
    }

    @Override
    public void handle(GrayEventMsg eventMsg) {
        if (!Objects.equals(eventMsg.getSourceType(), SourceType.GRAY_INSTANCE)) {
            return;
        }

        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoInitiralizer.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            if (StringUtils.equals(eventMsg.getServiceId(), instanceLocalInfo.getServiceId())){
                return;
            }
        }


        if(Objects.equals(eventMsg.getEventType(), EventType.UPDATE)){
            grayManager.updateGrayInstance((GrayInstance) eventMsg.getSource());
        }else{
            grayManager.closeGray(eventMsg.getServiceId(), eventMsg.getInstanceId());
        }
    }
}
