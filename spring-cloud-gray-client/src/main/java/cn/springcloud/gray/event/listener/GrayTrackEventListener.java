package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.event.GrayTrackEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2020-02-03 18:19
 */
public class GrayTrackEventListener extends AbstractGrayEventListener<GrayTrackEvent> {


    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;
    private GrayTrackHolder grayTrackHolder;

    public GrayTrackEventListener(
            InstanceLocalInfoObtainer instanceLocalInfoObtainer, GrayTrackHolder grayTrackHolder) {
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
        this.grayTrackHolder = grayTrackHolder;
    }

    @Override
    protected void onUpdate(GrayTrackEvent event) {
        grayTrackHolder.updateTrackDefinition(event.getSource());
    }

    @Override
    protected void onDelete(GrayTrackEvent event) {
        grayTrackHolder.deleteTrackDefinition(event.getSource().getName());
    }

    @Override
    protected boolean validate(GrayTrackEvent event) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        if (!StringUtils.equals(event.getServiceId(), instanceLocalInfo.getServiceId())) {
            return false;
        }
        if (StringUtils.isNotEmpty(event.getInstanceId())
                && !StringUtils.equals(event.getInstanceId(), instanceLocalInfo.getInstanceId())) {
            return false;
        }
        return true;
    }
}
