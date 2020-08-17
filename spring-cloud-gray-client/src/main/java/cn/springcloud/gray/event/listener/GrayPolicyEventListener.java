package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springlcoud.gray.event.GrayPolicyEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2020-02-03 18:18
 */
public class GrayPolicyEventListener extends AbstractGrayEventListener<GrayPolicyEvent> {


    private PolicyDecisionManager policyDecisionManager;

    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;


    public GrayPolicyEventListener(PolicyDecisionManager policyDecisionManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.policyDecisionManager = policyDecisionManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
    }

    @Override
    protected void onUpdate(GrayPolicyEvent event) {
        policyDecisionManager.setPolicyDefinition(event.getSource());
    }

    @Override
    protected void onDelete(GrayPolicyEvent event) {
        policyDecisionManager.removePolicy(event.getSourceId());
    }

    @Override
    protected boolean validate(GrayPolicyEvent event) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            if (StringUtils.equals(event.getServiceId(), instanceLocalInfo.getServiceId())) {
                return false;
            }
        }
        return true;
    }
}
