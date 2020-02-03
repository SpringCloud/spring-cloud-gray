package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springcloud.gray.model.GrayInstance;
import cn.springlcoud.gray.event.GrayInstanceEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2020-02-03 18:17
 */
public class GrayInstanceEventListener extends AbstractGrayEventListener<GrayInstanceEvent> {

    private GrayManager grayManager;
    private InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer;

    public GrayInstanceEventListener(
            GrayManager grayManager, InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoInitiralizer = instanceLocalInfoInitiralizer;
    }

    @Override
    protected void onUpdate(GrayInstanceEvent event) {
        grayManager.updateGrayInstance(event.getSource());
    }

    @Override
    protected void onDelete(GrayInstanceEvent event) {
        GrayInstance grayInstance = event.getSource();
        grayManager.closeGray(grayInstance.getServiceId(), grayInstance.getInstanceId());
    }


    @Override
    protected boolean validate(GrayInstanceEvent event) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoInitiralizer.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            GrayInstance grayInstance = event.getSource();
            if (StringUtils.equals(grayInstance.getServiceId(), instanceLocalInfo.getServiceId())) {
                return false;
            }
        }
        return true;
    }
}
