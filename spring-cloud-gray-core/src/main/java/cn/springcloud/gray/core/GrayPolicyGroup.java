package cn.springcloud.gray.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 灰度策略组，有状态属性
 */
public class GrayPolicyGroup {

    private String policyGroupId;
    private String alias;
    private List<GrayPolicy> list = new ArrayList<>();
    private boolean enable = true;

    public String getPolicyGroupId() {
        return policyGroupId;
    }

    public void setPolicyGroupId(String policyGroupId) {
        this.policyGroupId = policyGroupId;
    }

    public void addGrayPolicy(GrayPolicy policy) {
        removeGrayPolicy(policy.getPolicyId());
        list.add(policy);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<GrayPolicy> getList() {
        return list;
    }

    public void setList(List<GrayPolicy> list) {
        this.list = list;
    }

    public GrayPolicy removeGrayPolicy(String policyId) {
        Iterator<GrayPolicy> iter = list.iterator();
        while (iter.hasNext()) {
            GrayPolicy policy = iter.next();
            if (policy.getPolicyId().equals(policyId)) {
                iter.remove();
                return policy;
            }
        }
        return null;
    }

    public GrayPolicy getGrayPolicy(String policyId) {
        for (GrayPolicy grayPolicy : list) {
            if (grayPolicy.getPolicyId().equals(policyId)) {
                return grayPolicy;
            }
        }
        return null;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
