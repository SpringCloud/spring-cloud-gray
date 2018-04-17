package cn.springcloud.gray.server.resources.domain.vo;

import cn.springcloud.gray.core.GrayPolicy;

import java.util.List;

public class GrayPolicyGroupVO {
    private String serviceId;
    private String instanceId;
    private String appName;
    private String url;
    private String policyGroupId;
    private String alias;
    private List<GrayPolicy> policies;
    private boolean enable;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<GrayPolicy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<GrayPolicy> policies) {
        this.policies = policies;
    }

    public String getPolicyGroupId() {
        return policyGroupId;
    }

    public void setPolicyGroupId(String policyGroupId) {
        this.policyGroupId = policyGroupId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
