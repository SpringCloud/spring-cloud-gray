package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.core.DecisionDefinition;
import cn.springcloud.gray.core.PolicyDefinition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class GrayPolicyGroupFO {
    private String instanceId;
    private String policyGroupId;
    private String alias;
    private List<DecisionDefinition> policies;
    @ApiModelProperty("0:关闭, 1:启用")
    private boolean enable;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPolicyGroupId() {
        return policyGroupId;
    }

    public void setPolicyGroupId(String policyGroupId) {
        this.policyGroupId = policyGroupId;
    }

    public List<DecisionDefinition> getPolicies() {
        return policies;
    }

    public void setPolicies(List<DecisionDefinition> policies) {
        this.policies = policies;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public PolicyDefinition toGrayPolicyGroup() {
        PolicyDefinition policyGroup = new PolicyDefinition();
        policyGroup.setAlias(this.getAlias());
        policyGroup.setList(this.getPolicies());
        policyGroup.setEnable(this.isEnable());
        policyGroup.setPolicyGroupId(this.getPolicyGroupId());
        return policyGroup;
    }
}
