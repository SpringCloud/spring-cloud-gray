package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.GrayInstance;
import cn.springlcoud.gray.event.GrayInstanceRoutePolicyEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-03 18:17
 */
public class GrayInstanceRoutePolicyEventListener extends AbstractGrayEventListener<GrayInstanceRoutePolicyEvent> {

    private GrayManager grayManager;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;

    public GrayInstanceRoutePolicyEventListener(
            GrayManager grayManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    protected void onUpdate(GrayInstanceRoutePolicyEvent event) {
        GrayInstance grayInstance = grayManager.getGrayInstance(event.getServiceId(), event.getInstanceId());
        if (Objects.isNull(grayInstance)) {
            return;
        }
        grayInstance.addRoutePolicy(event.getPolicyId());
    }

    @Override
    protected void onDelete(GrayInstanceRoutePolicyEvent event) {
        GrayInstance grayInstance = grayManager.getGrayInstance(event.getServiceId(), event.getInstanceId());
        if (Objects.isNull(grayInstance)) {
            return;
        }
        grayInstance.removeRoutePolicy(event.getPolicyId());
    }


    @Override
    protected boolean validate(GrayInstanceRoutePolicyEvent event) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        return instanceLocalInfo == null || !StringUtils.equals(event.getServiceId(), instanceLocalInfo.getServiceId());
    }
}
